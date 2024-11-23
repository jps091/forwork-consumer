package forwork.forwork_consumer.api.consumer.seller;

import forwork.forwork_consumer.api.consumer.seller.message.SalesRequestResultMessage;
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
public class SalesRequestResultMailConsumer{

    private static final String USER_SELLER_RESULT = "user.seller.result.q";
    private final MailSender mailSender;
    protected final RabbitTemplate rabbitTemplate;
    @RabbitListener(queues = USER_SELLER_RESULT, containerFactory = "customRabbitListenerContainerFactory")
    public void sendSalesRequestResultMail(SalesRequestResultMessage message) {
        EmailValidator.validate(message);
        mailSender.send(message);
    }
}

