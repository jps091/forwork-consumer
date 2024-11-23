package forwork.forwork_consumer.api.consumer.buyer;

import forwork.forwork_consumer.api.consumer.buyer.message.BuyerMessage;
import forwork.forwork_consumer.api.infrastructure.mail.MailSender;
import forwork.forwork_consumer.api.infrastructure.redis.RedisStringUtils;
import forwork.forwork_consumer.api.service.OrderStatusUpdateService;
import forwork.forwork_consumer.api.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Component
@Slf4j
@RequiredArgsConstructor
public class BuyerMailConsumer {
    private static final String USER_BUYER = "user.buyer.q";
    private final MailSender mailSender;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final RedisStringUtils redisStringUtils;

    @RabbitListener(queues = USER_BUYER, containerFactory = "customRabbitListenerContainerFactory")
    public void sendBuyerMail(BuyerMessage message){
        EmailValidator.validate(message);
        mailSender.send(message);
        orderStatusUpdateService.updateOrderStatusSent(message);
        increaseResumeSalesQuantity(message);
    }

    private void increaseResumeSalesQuantity(BuyerMessage message) {
        String key = "resume:" + message.getResumeId();
        redisStringUtils.safeIncrement(key);
    }
}

