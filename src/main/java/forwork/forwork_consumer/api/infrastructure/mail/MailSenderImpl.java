package forwork.forwork_consumer.api.infrastructure.mail;

import forwork.forwork_consumer.api.consumer.MessageIfs;
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
    public void send(MessageIfs message) {
        SimpleMailMessage mailService = new SimpleMailMessage();
        mailService.setTo(message.getEmail());
        mailService.setSubject(message.getTitle());
        mailService.setText(message.getContent());
        javaMailSender.send(mailService);
    }
}
