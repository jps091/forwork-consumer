package forwork.forwork_consumer.api.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisStringUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public Set<String> getKeys(String key){
        return redisTemplate.keys(key);
    }

    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void setValueWithTimeout(String key, String value, Long ttlSeconds){
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    public void deleteKey(String key){
        if (key == null || key.isEmpty()) {
            return;
        }
        redisTemplate.delete(key);
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


    public void safeIncrement(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }

    public Map<String, Integer> syncAndResetKeys(Set<String> keys) {
        String script = """
            local result = {}
            for i, key in ipairs(KEYS) do
                local current = redis.call("GET", key)
                if current then
                    table.insert(result, key)
                    table.insert(result, tonumber(current))
                    redis.call("SET", key, "0")
                end
            end
            return result
        """;

        // RedisScript 선언
        RedisScript<List> redisScript = RedisScript.of(script, List.class);
        List<String> keyList = new ArrayList<>(keys);

        // Lua 스크립트 실행
        List<Object> result = redisTemplate.execute(redisScript, keyList);
        Map<String, Integer> syncData = new HashMap<>();

        // 결과 매핑
        if (result != null) {
            for (int i = 0; i < result.size(); i += 2) {
                String key = (String) result.get(i);
                Integer value = ((Long) result.get(i + 1)).intValue();
                syncData.put(key, value);
            }
        }
        return syncData;
    }
}
