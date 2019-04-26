package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.QUIZ)
public class Quiz extends BaseEntity {

  @Builder.Default
  private String id = UUID.randomUUID().toString();

  @Field(value = FieldName.Quiz.TITLE)
  private String title;

  @Field(value = FieldName.Quiz.DESCRIPTION)
  private String description;

  @Field(value = FieldName.Quiz.DEADLINE)
  private long deadline;

  @Field(value = FieldName.Quiz.TIME_LIMIT)
  private long timeLimit;

  @Field(value = FieldName.Quiz.TRIES)
  private int tries;

  //TODO uncomment this when question bank feature is complete
//  @DBRef
//    @Field(value = FieldName.Quiz.QUESTION_BANK)
//  private QuestionBank questionBank;

  @Field(value = FieldName.Quiz.QUESTION_COUNT)
  private int questionCount;
}
