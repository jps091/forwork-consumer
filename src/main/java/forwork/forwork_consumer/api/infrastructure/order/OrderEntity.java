package forwork.forwork_consumer.api.infrastructure.order;

import forwork.forwork_consumer.api.infrastructure.BaseTimeEntity;
import forwork.forwork_consumer.api.infrastructure.order.enums.OrderStatus;
import forwork.forwork_consumer.api.infrastructure.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") @NotNull
    private UserEntity userEntity;

    @Column(length = 25, name = "request_id") @NotNull
    private String requestId;

    @Column(precision = 8, name = "total_amount") @NotNull
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    @Column(name = "paid_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime paidAt;
}
