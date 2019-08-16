package com.future.function.web.model.request.core;

import com.future.function.common.data.core.UserData;
import com.future.function.validation.annotation.core.BatchMustExist;
import com.future.function.validation.annotation.core.EmailMustBeUnique;
import com.future.function.validation.annotation.core.FileMustBeImage;
import com.future.function.validation.annotation.core.FileMustExist;
import com.future.function.validation.annotation.core.Name;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;
import com.future.function.validation.annotation.core.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EmailMustBeUnique
@OnlyStudentCanHaveBatchAndUniversity
public class UserWebRequest implements UserData {

  private String id;

  @NotNull(message = "NotNull")
  private String role;

  @Email(message = "Email")
  @NotBlank(message = "NotBlank")
  private String email;

  @Name
  @NotBlank(message = "NotBlank")
  private String name;

  @Phone
  private String phone;

  @NotBlank(message = "NotBlank")
  private String address;

  @BatchMustExist
  private String batch;

  private String university;

  @Size(max = 1,
        message = "Size")
  @FileMustExist
  @FileMustBeImage
  private List<String> avatar;

}
