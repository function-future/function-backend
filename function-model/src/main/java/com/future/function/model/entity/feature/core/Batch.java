package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representation for batches.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.BATCH)
public class Batch extends BaseEntity {
  
  @Transient
  public static final String SEQUENCE_NAME = "batches_sequence";
  
  @Id
  private long number;
  
}
