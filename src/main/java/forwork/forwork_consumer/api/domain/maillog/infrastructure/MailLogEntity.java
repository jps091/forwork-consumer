package forwork.forwork_consumer.api.domain.maillog.infrastructure;

import forwork.forwork_consumer.api.common.infrastructure.BaseTimeEntity;
import forwork.forwork_consumer.api.domain.maillog.infrastructure.enums.EmailType;
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

    @Column(length = 25, name = "request_id")
    private String requestId;

    @Column(name = "resume_id")
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type")
    private EmailType emailType;

    @Column(length = 255, name = "error_response") // char
    private String errorResponse;

    public static MailLogEntity create(String email, String requestId, Long resumeId, Exception e){
        return MailLogEntity.builder()
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .emailType(EmailType.PURCHASE)
                .errorResponse(e.getMessage())
                .build();
    }

    public static MailLogEntity create(String email, EmailType type, Exception e){
        return MailLogEntity.builder()
                .email(email)
                .emailType(type)
                .errorResponse(e.getMessage())
                .build();
    }
}
