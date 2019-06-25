package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
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

  @Field(value = FieldName.Quiz.START_DATE)
  private long startDate;

  @Field(value = FieldName.Quiz.END_DATE)
  private long endDate;

  @Field(value = FieldName.Quiz.TIME_LIMIT)
  private long timeLimit;

  @Field(value = FieldName.Quiz.TRIALS)
  private int trials;

  @DBRef
  @Field(value = FieldName.Quiz.QUESTION_BANK)
  private List<QuestionBank> questionBanks;

  @Field(value = FieldName.Quiz.QUESTION_COUNT)
  private int questionCount;

  @DBRef
  @Field(value = FieldName.Quiz.BATCH)
  private Batch batch;
}
