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

import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.OPTION)
public class Option extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Field(FieldName.Option.LABEL)
    private String label;

    @Field(FieldName.Option.CORRECT)
    private boolean correct;

    @DBRef
    @Field(FieldName.Option.QUESTION)
    private Question question;
}
