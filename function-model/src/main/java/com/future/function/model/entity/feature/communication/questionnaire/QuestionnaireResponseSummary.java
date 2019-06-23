package com.future.function.model.entity.feature.communication.questionnaire;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTIONNAIRE_RESPONSE_SUMMARY)
public class QuestionnaireResponseSummary extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.QuestionnaireResponseSummary.QUESTIONNAIRE)
  @DBRef(lazy = true)
  private Questionnaire questionnaire;

  @Field(FieldName.QuestionnaireResponseSummary.APPRAISEE)
  @DBRef(lazy = true)
  private User appraisee;

  @Field(FieldName.QuestionnaireResponseSummary.SCORE_SUMMARY)
  private Answer scoreSummary;

  @Field(FieldName.QuestionnaireResponseSummary.COUNTER)
  private int counter;
}
