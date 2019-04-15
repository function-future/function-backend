package com.future.function.model.entity.feature.scoring;

import com.future.function.common.data.core.UserData;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.validation.annotation.core.Name;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;
import com.future.function.validation.annotation.core.Phone;
import java.util.UUID;
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
import org.springframework.format.annotation.NumberFormat;

/**
 * Entity representation for users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.ASSIGNMENT)
@OnlyStudentCanHaveBatchAndUniversity
public class Assignment extends BaseEntity {

  @Id
  @Builder.Default
  private String id = UUID.randomUUID().toString();

  @Email(message = "Email")
  @NotBlank(message = "NotBlank")
  private String title;

  @NotBlank(message = "NotBlank")
  private String description;

  @NotNull(message = "NotNull")
  private long deadline;

  @NotBlank
  private String question;

  private File file;

}
