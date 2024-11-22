package forwork.forwork_consumer.api.infrastructure.maillog;

import forwork.forwork_consumer.api.infrastructure.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "mail_logs")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailLogEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_log_id")
    private Long id;

    @Column(length = 50, name = "email") @NotNull
    private String email;


    @Column(length = 255, name = "error_response") // char
    private String errorResponse;

    @Column(length = 300, name = "message_content") // 직렬화된 메시지 내용
    private String messageContent;

    public static MailLogEntity create(String email, String content, Throwable e){
        return MailLogEntity.builder()
                .email(email)
                .messageContent(content)
                .errorResponse(e.getMessage())
                .build();
    }

    public static MailLogEntity create(String email, String content){
        return MailLogEntity.builder()
                .email(email)
                .messageContent(content)
                .build();
    }
}
