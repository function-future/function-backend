package com.future.function.model.entity.feature.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.Batch;
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
@Document(collection = DocumentName.USER_QUESTIONNAIRE_SUMMARY)
public class UserQuestionnaireSummary extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.UserQuestionnairesSummary.APPRAISEE)
  @DBRef(lazy = true)
  private User appraisee;

  @Field(FieldName.UserQuestionnairesSummary.BATCH)
  @DBRef(lazy = true)
  private Batch batch;

  @Field(FieldName.UserQuestionnairesSummary.ROLE)
  private Role role;

  @Field(FieldName.UserQuestionnairesSummary.SCORE_SUMMARY)
  private Answer scoreSummary;
}
