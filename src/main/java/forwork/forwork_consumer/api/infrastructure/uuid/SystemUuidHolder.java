package forwork.forwork_consumer.api.infrastructure.uuid;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString().substring(0, 7) + "@";
    }
}
