package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

  @Field(FieldName.Assignment.TITLE)
  private String title;

  @Field(FieldName.Assignment.DESCRIPTION)
  private String description;

  @Field(FieldName.Assignment.DEADLINE)
  private long deadline;

  @Field(FieldName.Assignment.FILE)
  private FileV2 file;

  @DBRef(lazy = true)
  @Field(FieldName.Assignment.BATCH)
  private Batch batch;

}
