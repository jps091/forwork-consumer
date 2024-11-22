package forwork.forwork_consumer.api.infrastructure.maillog.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailType {
    BUYER("구매자"),
    SELLER("판매자"),
    ADMIN("관리자"),
    VERIFY("인증"),
    NOTICE("공지"),
    PASSWORD("임시 비밀번호"),
    RETRY("재시도 실패")
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
