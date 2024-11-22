package forwork.forwork_consumer.api.consumer.notice;

import forwork.forwork_consumer.api.consumer.notice.message.NoticeMessage;
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
public class NoticeMailConsumer{

    private static final String USER_NOTICE = "user.notice.q";
    private final MailSender mailSender;
    protected final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = USER_NOTICE, containerFactory = "customRabbitListenerContainerFactory")
    public void sendSalesRequestResultMail(NoticeMessage message) {
        EmailValidator.validate(message);
        mailSender.send(message);
    }
}

