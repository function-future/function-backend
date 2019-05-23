package com.future.function.model.entity.feature.scoring;

import com.future.function.model.util.constant.DocumentName;
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
@Document(collection = DocumentName.STUDENT_QUIZ)
public class StudentQuestion {

    @Builder.Default
    private String id;

    @Field
    private Integer number;

    @DBRef
    @Field
    private Question question;

}
