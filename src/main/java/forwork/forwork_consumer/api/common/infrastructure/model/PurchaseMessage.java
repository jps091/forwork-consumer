package forwork.forwork_consumer.api.common.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseMessage {
    private String email;
    private String title;
    private String content;
    private Long orderId;
    private Long resumeId;
}
