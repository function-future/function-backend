package com.future.function.model.entity.base;

import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

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
  
  @Field(FieldName.BaseEntity.CREATED_AT)
  @CreatedDate
  private Long createdAt;
  
  @Field(FieldName.BaseEntity.CREATED_BY)
  @CreatedBy
  private String createdBy;
  
  @Field(FieldName.BaseEntity.UPDATED_AT)
  @LastModifiedDate
  private Long updatedAt;
  
  @Field(FieldName.BaseEntity.UPDATED_BY)
  @LastModifiedBy
  private String updatedBy;
  
  @Field(FieldName.BaseEntity.DELETED)
  private boolean deleted;
  
}
