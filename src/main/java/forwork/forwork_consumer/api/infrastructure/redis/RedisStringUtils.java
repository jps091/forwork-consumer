package forwork.forwork_consumer.api.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisStringUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public void setDataWithTimeout(String key, String value, Long ttlSeconds){
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }
    public String createKeyForm(String prefix, Object value) {
        return prefix + value;
    }

    public Boolean lock(Long key){
        return redisTemplate.opsForValue()
                .setIfAbsent(generateKey(key), "lock", Duration.ofSeconds(3));
    }

    public Boolean unLock(Long key){
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
