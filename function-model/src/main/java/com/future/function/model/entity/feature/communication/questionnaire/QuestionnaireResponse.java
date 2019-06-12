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

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTIONNAIRE_RESPONSE)
public class QuestionnaireResponse extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.QuestionnaireResponse.QUESTIONNAIRE)
  @DBRef(lazy = true)
  private Questionnaire questionnaire;

  @Field(FieldName.QuestionnaireResponse.APPRAISER)
  @DBRef(lazy = true)
  private User appraiser;

  @Field(FieldName.QuestionnaireResponse.APPRAISEE)
  @DBRef(lazy = true)
  private User appraisee;

  @Field(FieldName.QuestionnaireResponse.SCORE_SUMMARY)
  @DBRef
  private Answer scoreSummary;

  @Field(FieldName.QuestionnaireResponse.DETAILS)
  @DBRef(lazy = true)
  private List<QuestionResponse> details;

}
