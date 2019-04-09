package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;

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
  
  @Id
  private String id;
  
  @Min(value = 1,
       message = "Min")
  private long number;
  
  private boolean deleted;
  
}
