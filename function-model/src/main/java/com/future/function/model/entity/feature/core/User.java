package com.future.function.model.entity.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.USER)
public class User extends BaseEntity {
  
  @Id
  private String id;
  
  @Field(FieldName.User.EMAIL)
  private String email;
  
  @Field(FieldName.User.NAME)
  private String name;
  
  @Field(FieldName.User.PASS)
  private String password;
  
  @Field(FieldName.User.ROLE)
  private Role role;
  
  @Field(FieldName.User.PICTURE)
  @DBRef(lazy = true)
  private FileV2 pictureV2;
  
  @Field(FieldName.User.PHONE)
  private String phone;
  
  @Field(FieldName.User.ADDRESS)
  private String address;
  
  @Field(FieldName.User.BATCH)
  @DBRef(lazy = true)
  private Batch batch;
  
  @Field(FieldName.User.UNIVERSITY)
  private String university;
  
}
