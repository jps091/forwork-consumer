package forwork.forwork_consumer.api.consumer.admin;

import forwork.forwork_consumer.api.consumer.admin.message.AdminInquiryMessage;
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
public class AminInquiryMailConsumer{

    public static final String USER_ADMIN_INQUIRY = "user.admin.inquiry.q";
    private final MailSender mailSender;
    protected final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = USER_ADMIN_INQUIRY, containerFactory = "customRabbitListenerContainerFactory")
    public void sendVerifyCodeMail(AdminInquiryMessage message) {
        EmailValidator.validate(message);
        mailSender.sendBySes(message);
    }
}

