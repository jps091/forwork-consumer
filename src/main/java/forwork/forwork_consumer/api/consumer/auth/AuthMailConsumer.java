package forwork.forwork_consumer.api.consumer.auth;

import forwork.forwork_consumer.api.consumer.auth.message.AuthMessage;
import forwork.forwork_consumer.api.infrastructure.redis.RedisStringUtils;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.uuid.UuidHolder;
import forwork.forwork_consumer.api.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class AuthMailConsumer {

    private static final String VERIFY = "verify.q";
    private static final String EMAIL_PREFIX = "email:";
    private final MailSender mailSender;
    private final RedisStringUtils redisStringUtils;
    private final UuidHolder uuidHolder;

    @RabbitListener(queues = VERIFY, containerFactory = "customRabbitListenerContainerFactory")
    public void sendVerifyCodeMail(String email) {
            AuthMessage authMessage = createMessage(email);
            EmailValidator.validate(authMessage);
            mailSender.send(authMessage);
    }

    private AuthMessage createMessage(String email){
        String certificationCode = issueCertificationCode(email);
        return AuthMessage.builder()
                .email(email)
                .title("for-work 인증 코드 발송")
                .content("인증코드 : " + certificationCode)
                .build();
    }

    private String issueCertificationCode(String email){
        String certificationCode = uuidHolder.random();
        String key = getKeyByEmail(email);
        redisStringUtils.setValueWithTimeout(key, certificationCode, 300L);

        return certificationCode;
    }

    private String getKeyByEmail(String email) {
        return redisStringUtils.createKeyForm(EMAIL_PREFIX, email);
    }
}

