package forwork.forwork_consumer.api.consumer.deadletter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import forwork.forwork_consumer.api.consumer.deadletter.message.FailMessage;
import forwork.forwork_consumer.api.service.MailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Component
@Slf4j
public class DlqConsumer {

    private static final String RETRY_COUNT_HEADER = "x-retries_count";
    private static final String RETRY = "retry.q";

    private final RabbitTemplate rabbitTemplate;
    private final MailLogService mailLogService;
    private final ObjectMapper objectMapper;

    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private int retryCount;

    @RabbitListener(queues = RETRY, containerFactory = "customRabbitListenerContainerFactory")
    public void processFailedMessagesRequeue(Message failedMessage) {
        Integer retriesCnt = (Integer) failedMessage.getMessageProperties().getHeaders()
                .get(RETRY_COUNT_HEADER);
        if (retriesCnt == null) {
            retriesCnt = 0;
        }
        if (retriesCnt >= retryCount) {
            retryMailLogRegister(failedMessage);
            log.error("DLQ retry fail message discarding");
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
        log.info("Retrying message for the {} time", retriesCnt);
        rabbitTemplate.send(originalExchange, originalRoutingKey, failedMessage);
    }

    private void retryMailLogRegister(Message failedMessage) {
        String messageBody = new String(failedMessage.getBody(), StandardCharsets.UTF_8);
        FailMessage message = parsing(messageBody);
        mailLogService.registerFailLog(message);
    }

    private FailMessage parsing(String messageBody) {
        try {
            return objectMapper.readValue(messageBody, FailMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Json Parsing ={}", messageBody);
            throw new IllegalStateException("Json Parsing Error");
        }
    }
}

