package forwork.forwork_consumer.api.domain.orderresume.service;

import com.rabbitmq.client.Channel;
import forwork.forwork_consumer.api.common.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.domain.maillog.service.MailLogService;
import forwork.forwork_consumer.api.domain.orderresume.infrastructure.OrderResumeMailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderResumeMailConsumer {

    public static final String MAIL_QUEUE = "mail.queue";
    private final MailSender mailSender;
    private final MailLogService mailLogService;
    private final OrderStatusUpdateService orderStatusUpdateService;

    @RabbitListener(queues = MAIL_QUEUE, ackMode = "MANUAL")
    public void orderResumeConsumer(
            OrderResumeMailMessage message, Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) {
        String title = "for-work 구매 이력서 : " + message.getSalesPostTitle();
        String content = "주문 번호 #" + message.getOrderId() + " <URL> : " + message.getResumeUrl();

        try {
            mailSender.send(message.getEmail(), title, content);
            mailLogService.registerSuccessLog(message);
            orderStatusUpdateService.updateOrderStatusSent(message);
            log.info("orderResumeConsumer={}", message);
            channel.basicAck(tag, false);

        } catch (Exception e) {
            // 이메일 발송 실패 시 로그 기록 및 Nack 전송 시도
            log.error("Exception occurred while sending email for orderId={}", message.getOrderId(), e);
            mailLogService.registerFailLog(message, e);

            try {
                // Nack 전송으로 메시지를 큐에 남겨 재시도하도록 설정
                channel.basicNack(tag, false, true);
            } catch (IOException nackException) {
                // Nack 전송 실패 시 예외 로깅
                log.error("Failed to send Nack for orderId={}", message.getOrderId(), nackException);
            }
        }
    }
}

