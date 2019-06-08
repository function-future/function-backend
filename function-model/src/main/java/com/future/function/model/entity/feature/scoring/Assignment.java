package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.util.constant.DocumentName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Entity representation for assignments.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.ASSIGNMENT)
public class Assignment extends BaseEntity {

  @Id
  @Builder.Default
  private String id = UUID.randomUUID().toString();

  private String title;

  private String description;

  private long deadline;

  @DBRef
  private FileV2 file;

}
