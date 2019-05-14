package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.STUDENT_QUIZ_DETAIL)
public class StudentQuizDetail extends BaseEntity {

    @Field(FieldName.StudentQuizDetail.ID)
    private String id = UUID.randomUUID().toString();

    @DBRef
    @Field(FieldName.StudentQuizDetail.STUDENT_QUIZ)
    private StudentQuiz studentQuiz;

    @Field(FieldName.StudentQuizDetail.QUESTION_LIST)
    private List<Question> questions;

    @Field(FieldName.StudentQuizDetail.POINT)
    private int point;
}
