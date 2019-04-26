package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.util.constant.DocumentName;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

  private String question;

  private File file;

}
