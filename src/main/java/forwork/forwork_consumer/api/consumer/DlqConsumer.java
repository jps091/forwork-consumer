package forwork.forwork_consumer.api.consumer;

import forwork.forwork_consumer.api.service.MailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class DlqConsumer {

    private static final String RETRY_COUNT_HEADER = "x-retries_count";
    private static final String RETRY_QUEUE = "retry.queue";

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private int retryCount;

    @RabbitListener(queues = RETRY_QUEUE)
    public void processFailedMessagesRequeue(Message failedMessage) {
        Integer retriesCnt = (Integer) failedMessage.getMessageProperties().getHeaders()
                .get(RETRY_COUNT_HEADER);
        if (retriesCnt == null) {
            retriesCnt = 0;
        }
        if (retriesCnt >= retryCount) {
            log.error("Discarding message");
            return;
        }
        failedMessage.getMessageProperties().getHeaders().put(RETRY_COUNT_HEADER, ++retriesCnt);

        // 원래 교환기와 라우팅 키 정보를 사용하여 메시지 재전송
        String originalExchange = failedMessage.getMessageProperties().getReceivedExchange();
        String originalRoutingKey = failedMessage.getMessageProperties().getReceivedRoutingKey();

        if (originalExchange == null || originalExchange.isEmpty()) {
            log.error("exchange not valid={}", failedMessage);
            return;
        }

        rabbitTemplate.send(originalExchange, originalRoutingKey, failedMessage);
    }
}

