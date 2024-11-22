package forwork.forwork_consumer.api.consumer.password;

import forwork.forwork_consumer.api.consumer.password.message.TempPasswordMessage;
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
public class TempPasswordMailConsumer{

    public static final String USER_PASSWORD = "user.password.q";
    private final MailSender mailSender;
    protected final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = USER_PASSWORD, containerFactory = "customRabbitListenerContainerFactory")
    public void sendTempPasswordMail(TempPasswordMessage message) {
        EmailValidator.validate(message);
        mailSender.send(message);
    }
}

