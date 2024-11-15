package forwork.forwork_consumer.api.consumer.auth.message;

import forwork.forwork_consumer.api.consumer.MessageIfs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthMessage implements MessageIfs {
    private String email;
    private String title;
    private String content;
}
