package forwork.forwork_consumer.api.common.infrastructure.mail;

import forwork.forwork_consumer.api.common.infrastructure.model.PaymentMessage;
import forwork.forwork_consumer.api.common.infrastructure.model.PurchaseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private final JavaMailSender javaMailSender;
    @Override
    public void send(String email, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
    }

    @Override
    public void send(PurchaseMessage message) {
        SimpleMailMessage mailService = new SimpleMailMessage();
        mailService.setTo(message.getEmail());
        mailService.setSubject(message.getTitle());
        mailService.setText(message.getContent());
        javaMailSender.send(mailService);
    }

    @Override
    public void send(PaymentMessage message) {
        SimpleMailMessage mailService = new SimpleMailMessage();
        mailService.setTo(message.getEmail());
        mailService.setSubject(message.getTitle());
        mailService.setText(message.getContent());
        javaMailSender.send(mailService);
    }
}
