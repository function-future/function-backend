package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
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
@Document(collection = DocumentName.QUESTION)
public class Question extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Field(FieldName.Question.TEXT)
    private String text;

    @Field(FieldName.Question.QUESTION_BANK)
    @DBRef
    private QuestionBank questionBank;

    private List<Option> options;
}
