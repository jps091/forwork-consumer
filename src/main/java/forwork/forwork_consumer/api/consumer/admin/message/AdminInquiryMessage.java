package forwork.forwork_consumer.api.consumer.admin.message;

import forwork.forwork_consumer.api.consumer.MessageIfs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInquiryMessage implements MessageIfs {
    private String email;
    private String title;
    private String content;
}
