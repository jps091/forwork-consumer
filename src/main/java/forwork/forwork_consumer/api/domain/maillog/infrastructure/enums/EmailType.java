package forwork.forwork_consumer.api.domain.maillog.infrastructure.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailType {
    PAYMENT("결제"),
    PURCHASE("구매"),
    VERIFY("인증"),
    ;

    private String description;

    @JsonCreator
    public static EmailType from(String s) {
        for (EmailType status : EmailType.values()) {
            if (status.name().equalsIgnoreCase(s)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Result: " + s);
    }
}
