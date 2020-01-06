package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = DocumentName.REPORT_DETAIL)
public class ReportDetail extends BaseEntity {

  @Id
  @Builder.Default
  private String id = UUID.randomUUID()
    .toString();

  @Field(FieldName.ReportDetail.POINT)
  private int point;

  @DBRef(lazy = true)
  @Field(FieldName.ReportDetail.USER)
  private User user;

}
