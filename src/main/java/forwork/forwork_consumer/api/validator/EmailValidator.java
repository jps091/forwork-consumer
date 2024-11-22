package forwork.forwork_consumer.api.validator;

import forwork.forwork_consumer.api.consumer.MessageIfs;
import forwork.forwork_consumer.api.exception.InvalidEmailException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Component
public class EmailValidator {
    // 이메일 형식 확인을 위한 정규 표현식
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // 이메일 형식과 제목, 내용을 검증하는 메서드
    public static void validate(MessageIfs message) {
        // 이메일 형식 검증
        if (message.getEmail() == null || !EMAIL_PATTERN.matcher(message.getEmail()).matches()) {
            throw new InvalidEmailException("Invalid email format: " + message.getEmail());
        }

        // 제목 검증
        if (message.getTitle() == null || message.getTitle().trim().isEmpty()) {
            throw new InvalidEmailException("Email title cannot be null or blank.");
        }

//        if(message.getEmail().equals("jaepill94@gmail.com")){
//            throw new RuntimeException("test jaepil blank.");
//        }

        // 내용 검증
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new InvalidEmailException("Email content cannot be null or blank.");
        }
    }
}
