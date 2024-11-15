package forwork.forwork_consumer.api.service;

import forwork.forwork_consumer.api.infrastructure.clock.ClockHolder;
import forwork.forwork_consumer.api.consumer.buyer.message.BuyerMessage;
import forwork.forwork_consumer.api.infrastructure.orderresume.OrderResumeEntity;
import forwork.forwork_consumer.api.infrastructure.orderresume.OrderResumeJpaRepository;
import forwork.forwork_consumer.api.infrastructure.resume.ResumeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateService {

    private final ResumeQuantityService resumeQuantityService;
    private final OrderResumeJpaRepository orderResumeJpaRepository;
    private final ClockHolder clockHolder;

    @Transactional
    public void updateOrderStatusSent(BuyerMessage message) {
        List<OrderResumeEntity> orderResumes = orderResumeJpaRepository
                .findByOrderEntity_IdAndResumeEntity_Id(message.getOrderId(), message.getResumeId());
        orderResumes.forEach(orderResume -> orderResume.updateStatusSent(clockHolder));

        // resumeIds 추출
        List<Long> resumeIds = orderResumes.stream()
                .map(OrderResumeEntity::getResumeEntity)
                .map(ResumeEntity::getId)
                .toList();

        resumeQuantityService.addSalesQuantityWithOnePessimistic(resumeIds); // 판매량 1증가
    }
}
