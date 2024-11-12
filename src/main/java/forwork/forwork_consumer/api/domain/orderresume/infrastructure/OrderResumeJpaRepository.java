package forwork.forwork_consumer.api.domain.orderresume.infrastructure;


import forwork.forwork_consumer.api.domain.order.infrastructure.OrderEntity;
import forwork.forwork_consumer.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderResumeJpaRepository extends JpaRepository<OrderResumeEntity, Long> {
    List<OrderResumeEntity> findByOrderEntity_IdAndResumeEntity_Id(@Param("orderId") Long orderId, @Param("resumeId") Long resumeId);
}
