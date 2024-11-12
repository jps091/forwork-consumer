package forwork.forwork_consumer.api.domain.orderresume.infrastructure;

import forwork.forwork_consumer.api.common.infrastructure.enums.FieldType;
import forwork.forwork_consumer.api.common.infrastructure.enums.LevelType;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OrderResumeMailMessage {

    private Long orderId;
    private Long resumeId;
    private String email;
    private LevelType level;
    private FieldType field;
    private String resumeUrl;

    public String getSalesPostTitle(){
        return level.getDescription() + " " + field.getDescription() + " 이력서 #" + getResumeId();
    }
}
