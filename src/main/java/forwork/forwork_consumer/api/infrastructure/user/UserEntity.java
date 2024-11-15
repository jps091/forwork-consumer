package forwork.forwork_consumer.api.infrastructure.user;

import forwork.forwork_consumer.api.infrastructure.BaseTimeEntity;
import forwork.forwork_consumer.api.infrastructure.user.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 20) @NotNull
    private String password;

    @Column(length = 10) @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") @NotNull
    private UserStatus status;

    @Column(name = "last_login_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLoginAt;
}
