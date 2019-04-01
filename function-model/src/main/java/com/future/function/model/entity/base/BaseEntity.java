package com.future.function.model.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Base entity, to be used in auditing and to be extended by other entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
  
  private Date createdAt;
  
  private String createdBy;
  
  private Date updatedAt;
  
  private String updatedBy;
  
}
