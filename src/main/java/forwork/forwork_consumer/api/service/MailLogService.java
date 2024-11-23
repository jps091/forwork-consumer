package forwork.forwork_consumer.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import forwork.forwork_consumer.api.consumer.MessageIfs;
import forwork.forwork_consumer.api.infrastructure.maillog.MailLogEntity;
import forwork.forwork_consumer.api.infrastructure.maillog.MailLogJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class MailLogService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MailLogJpaRepository mailLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerFailLog(MessageIfs message, Throwable e){
        String content = setMessageContent(message);
        MailLogEntity mailLog = MailLogEntity.create(message.getEmail(), content, e);
        mailLogRepository.save(mailLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerFailLog(MessageIfs message){
        String content = setMessageContent(message);
        MailLogEntity mailLog = MailLogEntity.create(message.getEmail(), content);
        mailLogRepository.save(mailLog);
    }

    private String setMessageContent(MessageIfs message) {
        String mailContent = null;
        try {
            String title = "title : " + objectMapper.writeValueAsString(message.getTitle());
            String content = " content : " + objectMapper.writeValueAsString(message.getContent());
            mailContent = title + content;
            return mailContent;
        } catch (JsonProcessingException e) {
            mailContent = "JSON 직렬화 오류: " + e.getMessage();
            return  mailContent;
        }
    }
}
