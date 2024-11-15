package forwork.forwork_consumer.api.consumer.buyer;

import forwork.forwork_consumer.api.consumer.buyer.message.BuyerMessage;
import forwork.forwork_consumer.api.exception.InvalidEmailException;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.maillog.enums.EmailType;
import forwork.forwork_consumer.api.service.MailLogService;
import forwork.forwork_consumer.api.service.OrderStatusUpdateService;
import forwork.forwork_consumer.api.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class BuyerMailConsumer {

    private static final String USER_BUYER_QUEUE = "user.buyer.queue";
    private final MailSender mailSender;
    private final MailLogService mailLogService;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = USER_BUYER_QUEUE)
    public void sendBuyerMail(BuyerMessage message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        try {
            log.info("sendBuyerMail={}", message);
            EmailValidator.validate(message);
            mailSender.send(message);
            orderStatusUpdateService.updateOrderStatusSent(message);
        }catch (MailAuthenticationException | InvalidEmailException e) {
            handleDiscardException(message, deliveryTag, e);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    private void handleDiscardException(BuyerMessage message, long deliveryTag, Exception e) {
        String email = message.getEmail();
        log.error("Exception occurred while sending mail to {}, discarding message", email, e);
        mailLogService.registerFailLog(message, EmailType.BUYER, e);
        rabbitTemplate.execute(channel -> {
            channel.basicReject(deliveryTag, false);
            return null;
        });
    }
}

