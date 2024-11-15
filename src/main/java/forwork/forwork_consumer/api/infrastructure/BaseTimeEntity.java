package forwork.forwork_consumer.api.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
public class BaseTimeEntity {

    @Column(updatable = false, name = "registered_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime registeredAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        registeredAt = now;
        modifiedAt = now;
    }

    @PreUpdate
    public void preUpdate(){
        modifiedAt = LocalDateTime.now().withSecond(0).withNano(0);
    }
}