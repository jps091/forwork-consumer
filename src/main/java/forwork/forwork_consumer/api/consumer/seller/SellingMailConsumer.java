package forwork.forwork_consumer.api.consumer.seller;

import forwork.forwork_consumer.api.consumer.MailConsumer;
import forwork.forwork_consumer.api.consumer.seller.message.SellingMessage;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.maillog.enums.EmailType;
import forwork.forwork_consumer.api.service.MailLogService;
import forwork.forwork_consumer.api.consumer.seller.message.SalesRequestResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SellingMailConsumer extends MailConsumer<SellingMessage> {

    private static final String USER_SELLER_SELLING = "user.seller.selling.q";

    public SellingMailConsumer(MailSender mailSender, MailLogService mailLogService, RabbitTemplate rabbitTemplate) {
        super(mailSender, mailLogService, rabbitTemplate);
    }

    @RabbitListener(queues = USER_SELLER_SELLING, containerFactory = "customRabbitListenerContainerFactory")
    public void sendSellingMail(SellingMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        consumeMessage(message, EmailType.NOTICE, deliveryTag);
    }
}

