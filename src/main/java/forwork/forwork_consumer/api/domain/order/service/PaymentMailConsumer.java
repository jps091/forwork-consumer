package forwork.forwork_consumer.api.domain.order.service;

import com.rabbitmq.client.Channel;
import forwork.forwork_consumer.api.common.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.common.infrastructure.model.PaymentMessage;
import forwork.forwork_consumer.api.common.infrastructure.model.PurchaseMessage;
import forwork.forwork_consumer.api.domain.maillog.service.MailLogService;
import forwork.forwork_consumer.api.domain.orderresume.service.OrderStatusUpdateService;
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
public class PaymentMailConsumer {

    public static final String PAYMENT_MAIL_QUEUE = "payment.mail.queue";
    private final MailSender mailSender;
    private final MailLogService mailLogService;

    @RabbitListener(queues = PAYMENT_MAIL_QUEUE, ackMode = "MANUAL")
    public void sendPaymentMail(
            PaymentMessage message, Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) {
        try {
            mailSender.send(message);
            log.info("sendPaymentMail={}", message);
            channel.basicAck(tag, false);

        } catch (Exception e) {
            // 이메일 발송 실패 시 로그 기록 및 Nack 전송 시도
            log.error("Exception occurred while sending PaymentMail={}", message.getEmail(), e);
            mailLogService.registerFailLog(message, e);

            try {
                // Nack 전송으로 메시지를 큐에 남겨 재시도하도록 설정
                channel.basicNack(tag, false, true);
            } catch (IOException nackException) {
                // Nack 전송 실패 시 예외 로깅
                log.error("Failed to send Nack for Email={}", message.getEmail(), nackException);
            }
        }
    }
}

