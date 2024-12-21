package forwork.forwork_consumer.api.service;

import forwork.forwork_consumer.api.infrastructure.redis.RedisStringUtils;
import forwork.forwork_consumer.api.infrastructure.resume.ResumeEntity;
import forwork.forwork_consumer.api.infrastructure.resume.ResumeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ResumeQuantityService {

    private final ResumeJpaRepository resumeRepository;
    private final RedisStringUtils redisStringUtils;

    @Scheduled(fixedRate = 600000) //10분
    public void syncRedisToDB(){
        Set<String> keys = redisStringUtils.getKeys("resume:*");
        if (keys == null || keys.isEmpty()) {
            log.info("No keys found");
            return;
        }

        Map<String, Integer> syncData = redisStringUtils.syncAndResetKeys(keys);
        syncData.forEach((key, quantity) -> {
            Long resumeId = Long.valueOf(key.split(":")[1]);
            resumeRepository.updateSalesQuantity(resumeId, quantity);
        });

        //
        syncData.keySet().forEach(key -> {
            if ("0".equals(redisStringUtils.getValue(key))) {
                redisStringUtils.deleteKey(key);
            }
        });
    }
}
