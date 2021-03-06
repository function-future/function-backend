package com.future.function.model.entity.feature.communication.questionnaire;

import com.future.function.model.entity.base.BaseEntity;
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTION_QUESTIONNAIRE)
public class QuestionQuestionnaire extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.QuestionQuestionnaire.QUESTIONNAIRE)
  @DBRef(lazy = true)
  private Questionnaire questionnaire;

  @Field(FieldName.QuestionQuestionnaire.DESCRIPTION)
  private String description;

}
