package forwork.forwork_consumer.api.service;

import forwork.forwork_consumer.api.infrastructure.resume.ResumeEntity;
import forwork.forwork_consumer.api.infrastructure.resume.ResumeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ResumeQuantityService {

    private final ResumeJpaRepository resumeRepository;

    public void addSalesQuantityWithOnePessimistic(List<Long> resumeIds){
        for (Long resumeId : resumeIds) {
            ResumeEntity resumeEntity = resumeRepository.findByIdWithPessimisticLock(resumeId).orElseThrow();
            resumeEntity.increaseSalesQuantity();
        }
    }
}
