package com.future.function.model.entity.base;

import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

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

  @Field(FieldName.BaseEntity.VERSION)
  @Version
  private Long version;

}
