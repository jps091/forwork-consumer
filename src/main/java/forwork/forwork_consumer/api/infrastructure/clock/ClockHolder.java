package forwork.forwork_consumer.api.infrastructure.clock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface ClockHolder {
    long millis();
    long convertSecondsFrom(long minutes);
    LocalDateTime now();
    LocalDate nowDate();
    Date convertExpiredDateFrom(long millis);
}
