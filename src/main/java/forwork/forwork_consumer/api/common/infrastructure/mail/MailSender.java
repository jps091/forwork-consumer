package forwork.forwork_consumer.api.common.infrastructure.mail;

public interface MailSender {
    void send(String email, String title, String content);
}
