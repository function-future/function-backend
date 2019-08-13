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
@Document(collection = DocumentName.OPTION)
public class Option extends BaseEntity {

  @Builder.Default
  private String id = UUID.randomUUID()
    .toString();

  @Field(FieldName.Option.LABEL)
  private String label;

  @Field(FieldName.Option.CORRECT)
  private boolean correct;

  @DBRef(lazy = true)
  @Field(FieldName.Option.QUESTION)
  private Question question;

}
