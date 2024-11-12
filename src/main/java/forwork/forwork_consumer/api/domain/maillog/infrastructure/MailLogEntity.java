package forwork.forwork_consumer.api.domain.maillog.infrastructure;

import forwork.forwork_consumer.api.common.infrastructure.BaseTimeEntity;
import forwork.forwork_consumer.api.domain.maillog.infrastructure.enums.Result;
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

    @Column(length = 25, name = "request_id") @NotNull
    private String requestId;

    @Column(name = "resume_id") @NotNull
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Result result;

    @Column(length = 255, name = "error_response") // char
    private String errorResponse;

    public static MailLogEntity create(String email, String requestId, Long resumeId, Exception e){
        return MailLogEntity.builder()
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .result(Result.FAIL)
                .errorResponse(e.getMessage())
                .build();
    }

    public static MailLogEntity create(String email, String requestId, Long resumeId){
        return MailLogEntity.builder()
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .result(Result.SUCCESS)
                .build();
    }
}
