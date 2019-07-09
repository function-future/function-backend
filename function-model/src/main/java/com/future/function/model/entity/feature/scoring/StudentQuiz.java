package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
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
@Document(collection = DocumentName.STUDENT_QUIZ)
public class StudentQuiz extends BaseEntity {

  @Builder.Default
  private String id = UUID.randomUUID().toString();

  @DBRef(lazy = true)
  @Field(FieldName.StudentQuiz.STUDENT)
  private User student;

  @Field(FieldName.StudentQuiz.QUIZ)
  private Quiz quiz;

  @Field(FieldName.StudentQuiz.TRIALS)
  private int trials;

  @Field(FieldName.StudentQuiz.DONE)
  private boolean done;
}
