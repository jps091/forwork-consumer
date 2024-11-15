package forwork.forwork_consumer.api.consumer;

import forwork.forwork_consumer.api.exception.InvalidEmailException;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.maillog.enums.EmailType;
import forwork.forwork_consumer.api.service.MailLogService;
import forwork.forwork_consumer.api.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.messaging.handler.annotation.Header;
@Slf4j
@RequiredArgsConstructor
public abstract class MailConsumer<T extends MessageIfs> {

    protected final MailSender mailSender;
    protected final MailLogService mailLogService;
    protected final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private int retryCount;

    public void consumeMessage(T message, EmailType type, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Consuming message={}", message);
            EmailValidator.validate(message);
            mailSender.send(message);
        } catch (MailAuthenticationException | InvalidEmailException e) {
            handleDiscardException(message, type, deliveryTag, e);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    private void handleDiscardException(T message, EmailType type, long deliveryTag, Exception e) {
        String email = message.getEmail();
        log.error("Exception occurred while sending mail to {}, discarding message", email, e);
        mailLogService.registerFailLog(message, type, e);
        rabbitTemplate.execute(channel -> {
            channel.basicReject(deliveryTag, false);
            return null;
        });
    }
}