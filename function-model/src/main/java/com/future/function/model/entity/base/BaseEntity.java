package com.future.function.model.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Base entity, to be used in auditing and to be extended by other entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
  
  @CreatedDate
  private Long createdAt;
  
  @CreatedBy
  private String createdBy;
  
  @LastModifiedDate
  private Long updatedAt;
  
  @LastModifiedBy
  private String updatedBy;
  
  private boolean deleted;
  
}
