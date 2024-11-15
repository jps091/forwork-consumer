package forwork.forwork_consumer.api.consumer.notice;

import forwork.forwork_consumer.api.consumer.MailConsumer;
import forwork.forwork_consumer.api.consumer.notice.message.NoticeMessage;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.maillog.enums.EmailType;
import forwork.forwork_consumer.api.service.MailLogService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoticeMailConsumer extends MailConsumer<NoticeMessage> {

    private static final String USER_NOTICE_QUEUE = "user.notice.queue";

    public NoticeMailConsumer(MailSender mailSender, MailLogService mailLogService, RabbitTemplate rabbitTemplate) {
        super(mailSender, mailLogService, rabbitTemplate);
    }

    @RabbitListener(queues = USER_NOTICE_QUEUE)
    public void sendSalesRequestResultMail(NoticeMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        consumeMessage(message, EmailType.NOTICE, deliveryTag);
    }
}

