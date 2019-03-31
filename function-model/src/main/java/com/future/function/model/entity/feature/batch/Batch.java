package com.future.function.model.entity.feature.batch;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.DocumentName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.BATCH)
public class Batch extends BaseEntity {
  
  @Id
  private String id;
  
  private long number;
  
  private boolean deleted;
  
}
