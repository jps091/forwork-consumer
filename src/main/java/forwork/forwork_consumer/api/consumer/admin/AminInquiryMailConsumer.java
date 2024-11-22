package forwork.forwork_consumer.api.consumer.admin;

import forwork.forwork_consumer.api.consumer.MailConsumer;
import forwork.forwork_consumer.api.consumer.admin.message.AdminInquiryMessage;
import forwork.forwork_consumer.api.consumer.auth.message.AuthMessage;
import forwork.forwork_consumer.api.consumer.password.message.TempPasswordMessage;
import forwork.forwork_consumer.api.exception.CustomSesException;
import forwork.forwork_consumer.api.exception.InvalidEmailException;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.maillog.enums.EmailType;
import forwork.forwork_consumer.api.service.MailLogService;
import forwork.forwork_consumer.api.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.model.SesException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AminInquiryMailConsumer{

    public static final String USER_ADMIN_INQUIRY = "user.admin.inquiry.q";
    private final MailSender mailSender;
    private final MailLogService mailLogService;
    protected final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = USER_ADMIN_INQUIRY, containerFactory = "customRabbitListenerContainerFactory")
    public void sendVerifyCodeMail(AdminInquiryMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            EmailValidator.validate(message);
            mailSender.send(message);
        } catch (CustomSesException e) {
            throw new CustomSesException("ses", e);
        }
    }
//    public void sendVerifyCodeMail(AdminInquiryMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
//        try {
//            EmailValidator.validate(message);
//            mailSender.sendBySes(message);
//
//        } catch (CustomSesException | InvalidEmailException e) {
//            handleDiscardException(message, deliveryTag, e);
//        } catch (Exception e) {
//            throw new AmqpRejectAndDontRequeueException(e);
//        }
//    }

    private void handleDiscardException(AdminInquiryMessage message, long deliveryTag, Exception e) {
        String email = message.getEmail();
        log.error("Exception occurred while sending mail to {}, discarding message", email, e);
        mailLogService.registerFailLog(message, EmailType.ADMIN, e);
        rabbitTemplate.execute(channel -> {
            channel.basicReject(deliveryTag, false);
            return null;
        });
    }
}

