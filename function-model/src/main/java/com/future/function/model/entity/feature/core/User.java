package com.future.function.model.entity.feature.core;

import com.future.function.common.data.core.UserData;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.future.function.validation.annotation.core.Name;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;
import com.future.function.validation.annotation.core.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

/**
 * Entity representation for users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.USER)
@OnlyStudentCanHaveBatchAndUniversity
public class User extends BaseEntity implements UserData {
  
  @Id
  private String id;
  
  @Field(FieldName.User.EMAIL)
  @Email(message = "Email")
  @NotBlank(message = "NotBlank")
  private String email;
  
  @Field(FieldName.User.NAME)
  @Name
  @NotBlank(message = "NotBlank")
  private String name;
  
  @Field(FieldName.User.PASSWORD)
  @NotBlank(message = "NotBlank")
  private String password;
  
  @Field(FieldName.User.ROLE)
  @NotNull(message = "NotNull")
  private Role role;
  
  @Field(FieldName.User.PICTURE)
  @DBRef(lazy = true)
  @NotNull(message = "NotNull")
  private File picture;
  
  @Field(FieldName.User.PHONE)
  @Phone
  private String phone;
  
  @Field(FieldName.User.ADDRESS)
  @NotBlank(message = "NotBlank")
  private String address;
  
  @Field(FieldName.User.BATCH)
  @DBRef(lazy = true)
  private Batch batch;
  
  @Field(FieldName.User.UNIVERSITY)
  private String university;
  
  @Override
  public String getRoleAsString() {
    
    return this.role.name();
  }
  
  @Override
  public Long getBatchNumber() {
    
    return this.batch != null ? this.batch.getNumber() : null;
  }
  
  @Override
  public String getUniversity() {
    
    return this.university;
  }
  
}
