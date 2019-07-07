package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Report extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Field(FieldName.Report.TITLE)
    private String title;

    @Field(FieldName.Report.DESCRIPTION)
    private String description;

    @Field(FieldName.Report.USED_AT)
    private LocalDate usedAt;

    @DBRef
    @Field(FieldName.Report.BATCH)
    private Batch batch;

    private List<String> studentIds;

}
