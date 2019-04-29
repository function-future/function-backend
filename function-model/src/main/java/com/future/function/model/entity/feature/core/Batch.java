package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entity representation for batches.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.BATCH)
public class Batch extends BaseEntity {
  
  @Transient
  public static final String SEQUENCE_NAME = "batches_sequence";
  
  @Id
  private String id;
@Field(FieldName.Batch.NUMBER)
  @NonNull
  private Long number;
  
}
