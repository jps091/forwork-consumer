package forwork.forwork_consumer.api.infrastructure.mail;

import forwork.forwork_consumer.api.consumer.MessageIfs;

public interface MailSender {
    void send(MessageIfs message);
    void sendBySes(MessageIfs message);
}
