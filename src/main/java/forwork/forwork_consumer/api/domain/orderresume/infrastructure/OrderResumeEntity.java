package forwork.forwork_consumer.api.domain.orderresume.infrastructure;

import forwork.forwork_consumer.api.common.infrastructure.BaseTimeEntity;
import forwork.forwork_consumer.api.common.infrastructure.clock.ClockHolder;
import forwork.forwork_consumer.api.domain.order.infrastructure.OrderEntity;
import forwork.forwork_consumer.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import forwork.forwork_consumer.api.domain.resume.infrastructure.ResumeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;

@Entity
@Table(name = "order_resumes")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResumeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") @NotNull
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") @NotNull
    private ResumeEntity resumeEntity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderResumeStatus status;

    @Column(name = "sent_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime sentAt;

    @Column(name = "canceled_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime canceledAt;

    public void updateStatusSent(ClockHolder clockHolder) {
        this.status = OrderResumeStatus.SENT;
        this.sentAt = clockHolder.now();
    }
}
