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

/**
 * Author : Ricky Kennedy
 * Created At : 19:41 12/11/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTION_RESPONSE_QUEUE)
public class QuestionResponseQueue extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.QuestionResponseQueue.QUESTION)
  @DBRef(lazy = true)
  private QuestionQuestionnaire question;

  @Field(FieldName.QuestionResponseQueue.APRAISER)
  @DBRef(lazy = true)
  private User appraiser;

  @Field(FieldName.QuestionResponseQueue.APRAISEE)
  @DBRef(lazy = true)
  private User appraisee;

  @Field(FieldName.QuestionResponseQueue.SCORE)
  private Float score;

  @Field(FieldName.QuestionResponseQueue.COMMENT)
  private String comment;

}
