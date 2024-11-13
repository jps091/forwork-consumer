package forwork.forwork_consumer.api.common.infrastructure.mail;

import forwork.forwork_consumer.api.common.infrastructure.model.PaymentMessage;
import forwork.forwork_consumer.api.common.infrastructure.model.PurchaseMessage;

public interface MailSender {
    void send(String email, String title, String content);

    void send(PurchaseMessage message);

    void send(PaymentMessage message);
}
