package com.future.function.model.entity.feature.communication.questionnaire;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTION_RESPONSE)
public class QuestionResponse extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.QuestionResponse.QUESTION)
  @DBRef(lazy = true)
  private QuestionQuestionnaire question;

  @Field(FieldName.QuestionResponse.APRAISER)
  @DBRef(lazy = true)
  private User appraiser;

  @Field(FieldName.QuestionResponse.APRAISEE)
  @DBRef(lazy = true)
  private User appraisee;

  @Field(FieldName.QuestionResponse.SCORE)
  private Float score;

  @Field(FieldName.QuestionResponse.COMMENT)
  private String comment;

}
