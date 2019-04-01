package com.future.function.model.entity.feature.user;

import com.future.function.common.data.UserData;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.file.FileInfo;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.Role;
import com.future.function.validation.annotation.OnlyStudentCanHaveBatchAndUniversity;
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
  
  @NotBlank(message = "NotBlank")
  private String name;
  
  @NotNull(message = "NotNull")
  private Role role;
  
  private FileInfo picture;
  
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
