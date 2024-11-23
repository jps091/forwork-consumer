package forwork.forwork_consumer.api.consumer.seller;

import forwork.forwork_consumer.api.consumer.seller.message.SellingMessage;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SellingMailConsumer{

    private static final String USER_SELLER_SELLING = "user.seller.selling.q";
    private final MailSender mailSender;
    protected final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = USER_SELLER_SELLING, containerFactory = "customRabbitListenerContainerFactory")
    public void sendSellingMail(SellingMessage message) {
        EmailValidator.validate(message);
        mailSender.send(message);
    }
}

