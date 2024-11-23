package forwork.forwork_consumer.api.infrastructure.mail;

import forwork.forwork_consumer.api.consumer.MessageIfs;
import forwork.forwork_consumer.api.exception.CustomSesException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    @Value("${aws.mail}")
    private String mail;
    private final JavaMailSender javaMailSender;
    private final SesClient sesClient;

    @Override
    public void send(MessageIfs message) {
        SimpleMailMessage mailService = new SimpleMailMessage();
        mailService.setTo(message.getEmail());
        mailService.setSubject(message.getTitle());
        mailService.setText(message.getContent());
        javaMailSender.send(mailService);
    }

    @Override
    public void sendBySes(MessageIfs message) {
        try {
            // SES 이메일 요청 생성
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(Destination.builder()
                            .toAddresses(message.getEmail()) // 수신자 이메일
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                .data(message.getTitle()) // 이메일 제목
                                .build())
                            .body(Body.builder()
                                .text(Content.builder()
                                .data(message.getContent()) // 이메일 본문
                                .build())
                                .build())
                            .build())
                    .source(mail) // SES에서 인증된 발신자 이메일
                    .build();

            // 이메일 전송
            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("Email sent successfully. Message ID: {}", response.messageId());
        } catch (Exception e) {
            throw new CustomSesException("Failed to SES", e);
        }
    }
}
