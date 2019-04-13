package com.future.function.model.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base entity, to be used in auditing and to be extended by other entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
  
  private Long createdAt;
  
  private String createdBy;
  
  private Long updatedAt;
  
  private String updatedBy;
  
  private boolean deleted;
  
}
