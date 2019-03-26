package com.future.function.model.entity.feature.users;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.file.FileInfo;
import com.future.function.model.util.DocumentName;
import com.future.function.model.util.constant.Role;
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
public class User extends BaseEntity {

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

}
