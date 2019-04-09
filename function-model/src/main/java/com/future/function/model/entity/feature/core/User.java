package com.future.function.model.entity.feature.core;

import com.future.function.common.data.core.UserData;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
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
  
  @Email(message = "Email")
  @NotBlank(message = "NotBlank")
  private String email;
  
  @Name
  @NotBlank(message = "NotBlank")
  private String name;
  
  @NotNull(message = "NotNull")
  private Role role;
  
  @DBRef(lazy = true)
  @NotNull(message = "NotNull")
  private File picture;
  
  @Phone
  private String phone;
  
  @NotBlank(message = "NotBlank")
  private String address;
  
  @DBRef(lazy = true)
  private Batch batch;
  
  private String university;
  
  private boolean deleted;
  
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
