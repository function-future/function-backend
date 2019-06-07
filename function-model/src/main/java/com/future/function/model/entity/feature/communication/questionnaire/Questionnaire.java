package com.future.function.model.entity.feature.communication.questionnaire;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTIONNAIRE)
public class Questionnaire extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.Questionnaire.TITLE)
  private String title;

  @Field(FieldName.Questionnaire.DESCRIPTION)
  private String description;

  @Field(FieldName.Questionnaire.START_DATE)
  private Long startDate;

}