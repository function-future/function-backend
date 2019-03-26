package com.future.function.model.entity.base;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

  private Date createdAt;

  private String createdBy;

  private boolean deleted;

  private Date updatedAt;

  private String updatedBy;

}
