package com.future.function.model.entity.feature.communication.questionnaire;

import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.ANSWER)
public class Answer {

    @Id
    private String id;

    @Field(FieldName.Answer.MIN)
    private Float minimum;

    @Field(FieldName.Answer.MAX)
    private Float maximum;

    @Field(FieldName.Answer.AVG)
    private Float average;
}
