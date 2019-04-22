package com.future.function.model.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Base entity class for auditing purposes and to be extended by other
 * entity representation classes.
 * <p>
 * This class' fields contains necessary and required information for other
 * entity representation classes. In addition, auditing is also implemented
 * by annotations in most fields in this class. The auditing process is done
 * automatically by the
 * {@link org.springframework.data.mongodb.config.EnableMongoAuditing}
 * annotation.
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
