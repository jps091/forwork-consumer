package forwork.forwork_consumer.api.infrastructure.orderresume;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderResumeJpaRepository extends JpaRepository<OrderResumeEntity, Long> {
    List<OrderResumeEntity> findByOrderEntity_IdAndResumeEntity_Id(@Param("orderId") Long orderId, @Param("resumeId") Long resumeId);
}
