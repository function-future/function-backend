package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = DocumentName.REPORT_DETAIL)
public class ReportDetail extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Field(FieldName.ReportDetail.POINT)
    private Integer point;

    @DBRef(lazy = true)
    @Field(FieldName.ReportDetail.USER)
    private User user;

    @DBRef(lazy = true)
    @Field(FieldName.ReportDetail.REPORT)
    private Report report;

}
