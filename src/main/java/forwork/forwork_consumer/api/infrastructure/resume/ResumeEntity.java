package forwork.forwork_consumer.api.infrastructure.resume;

import forwork.forwork_consumer.api.infrastructure.BaseTimeEntity;
import forwork.forwork_consumer.api.infrastructure.enums.FieldType;
import forwork.forwork_consumer.api.infrastructure.enums.LevelType;
import forwork.forwork_consumer.api.infrastructure.resume.enums.ResumeStatus;
import forwork.forwork_consumer.api.infrastructure.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.math.BigDecimal;

@Entity
@Table(name = "resumes")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id") @NotNull
    private UserEntity sellerEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "field") @NotNull
    private FieldType fieldType;

    @Enumerated(EnumType.STRING)
    @Column(name = "level") @NotNull
    private LevelType levelType;

    @NotNull
    @Column(length = 300, name = "resume_url")
    private String resumeUrl;

    @NotNull
    @Column(length = 300, name = "description_image_url")
    private String descriptionImageUrl;

    @NotNull
    @Column(precision = 6, scale = 0)
    private BigDecimal price;

    @Column(name = "sales_quantity") @NotNull
    private Integer salesQuantity;

    @Column(length = 5000) @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") @NotNull
    private ResumeStatus resumeStatus;

    public void increaseSalesQuantity(){
        this.salesQuantity =   this.salesQuantity + 1;
    }
}
