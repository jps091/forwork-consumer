package forwork.forwork_consumer.api.domain.maillog.service;

import forwork.forwork_consumer.api.common.infrastructure.model.PaymentMessage;
import forwork.forwork_consumer.api.common.infrastructure.model.PurchaseMessage;
import forwork.forwork_consumer.api.domain.maillog.infrastructure.MailLogEntity;
import forwork.forwork_consumer.api.domain.maillog.infrastructure.MailLogJpaRepository;
import forwork.forwork_consumer.api.domain.maillog.infrastructure.enums.EmailType;
import forwork.forwork_consumer.api.domain.order.infrastructure.OrderEntity;
import forwork.forwork_consumer.api.domain.order.infrastructure.OrderJpaRepository;
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
    public void registerFailLog(PurchaseMessage message, Exception e){
        OrderEntity order = orderRepository.findById(message.getOrderId()).orElseThrow();
        MailLogEntity mailLog = MailLogEntity.create(message.getEmail(), order.getRequestId(), message.getResumeId(), e);
        mailLogRepository.save(mailLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerFailLog(PaymentMessage message, Exception e){
        MailLogEntity mailLog = MailLogEntity.create(message.getEmail(), EmailType.PAYMENT, e);
        mailLogRepository.save(mailLog);
    }
}
