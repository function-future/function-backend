package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.QUESTION_BANK)
public class QuestionBank extends BaseEntity {

  @Builder.Default
  private String id = UUID.randomUUID().toString();

  private String description;
}
