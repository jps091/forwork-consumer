package forwork.forwork_consumer.api.infrastructure.order;

import forwork.forwork_consumer.api.infrastructure.order.enums.OrderStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByRequestId(String requestId);
    Optional<OrderEntity> findByUserEntity_IdAndId(Long userId, Long orderId);
    List<OrderEntity> findByUserEntity_Id(Long userId);
    List<OrderEntity> findByStatus(OrderStatus status, Limit limit);
}
