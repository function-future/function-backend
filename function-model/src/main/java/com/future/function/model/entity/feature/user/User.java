package com.future.function.model.entity.feature.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.future.function.common.UserData;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.file.FileInfo;
import com.future.function.model.util.DocumentName;
import com.future.function.model.util.constant.Role;
import com.future.function.validation.annotation.OnlyStudentCanHaveBatchAndUniversity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representation for users
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

  @Email
  private String email;

  @NotNull
  private String name;

  @NotNull
  private Role role;

  @NotNull
  private FileInfo picture;

  private String phone;

  @DBRef(lazy = true)
  private Batch batch;

  @NotNull
  private String address;

  private String university;

  private boolean deleted;

  @Override
  public Long getBatchNumber() {

    return this.batch != null ? this.batch.getNumber() : null;
  }

  @Override
  public String getRoleAsString() {

    return this.role.name();
  }

  @Override
  public String getUniversity() {

    return this.university;
  }

}
