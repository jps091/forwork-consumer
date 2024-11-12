package forwork.forwork_consumer.api.domain.maillog.service;

import forwork.forwork_consumer.api.domain.maillog.infrastructure.MailLogEntity;
import forwork.forwork_consumer.api.domain.maillog.infrastructure.MailLogJpaRepository;
import forwork.forwork_consumer.api.domain.order.infrastructure.OrderEntity;
import forwork.forwork_consumer.api.domain.order.infrastructure.OrderJpaRepository;
import forwork.forwork_consumer.api.domain.orderresume.infrastructure.OrderResumeMailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MailLogService {

    private final MailLogJpaRepository mailLogRepository;
    private final OrderJpaRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerFailLog(OrderResumeMailMessage body, Exception e){
        OrderEntity order = orderRepository.findById(body.getOrderId()).orElseThrow();
        MailLogEntity mailLog = MailLogEntity.create(body.getEmail(), order.getRequestId(), body.getResumeId(), e);
        mailLogRepository.save(mailLog);
    }

    @Transactional
    public void registerSuccessLog(OrderResumeMailMessage body){
        OrderEntity order = orderRepository.findById(body.getOrderId()).orElseThrow();
        MailLogEntity mailLog = MailLogEntity.create(body.getEmail(), order.getRequestId(), body.getResumeId());
        mailLogRepository.save(mailLog);
    }
}
