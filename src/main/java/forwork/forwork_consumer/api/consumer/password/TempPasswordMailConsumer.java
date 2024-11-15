package forwork.forwork_consumer.api.consumer.password;

import forwork.forwork_consumer.api.consumer.MailConsumer;
import forwork.forwork_consumer.api.consumer.auth.message.AuthMessage;
import forwork.forwork_consumer.api.consumer.password.message.TempPasswordMessage;
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
public class TempPasswordMailConsumer extends MailConsumer<TempPasswordMessage> {

    public static final String USER_TEMP_PASSWORD_QUEUE = "user.temp.password.queue";

    public TempPasswordMailConsumer(MailSender mailSender, MailLogService mailLogService, RabbitTemplate rabbitTemplate) {
        super(mailSender, mailLogService, rabbitTemplate);
    }

    @RabbitListener(queues = USER_TEMP_PASSWORD_QUEUE)
    public void sendTempPasswordMail(TempPasswordMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        consumeMessage(message, EmailType.PASSWORD, deliveryTag);
    }
}

