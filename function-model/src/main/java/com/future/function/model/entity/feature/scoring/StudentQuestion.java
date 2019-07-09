package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.STUDENT_QUESTION)
public class StudentQuestion extends BaseEntity {

  @Builder.Default
  private String id = UUID.randomUUID().toString();

  @Field(value = FieldName.StudentQuestion.STUDENT_QUIZ_DETAIL)
  private StudentQuizDetail studentQuizDetail;

  @Field(value = FieldName.StudentQuestion.NUMBER)
  private Integer number;

  @DBRef(lazy = true)
  @Field(value = FieldName.StudentQuestion.QUESTION)
  private Question question;

  @DBRef(lazy = true)
  @Field(value = FieldName.StudentQuestion.OPTION)
  private Option option;

  @Field(value = FieldName.StudentQuestion.CORRECT)
  private boolean correct;
}
