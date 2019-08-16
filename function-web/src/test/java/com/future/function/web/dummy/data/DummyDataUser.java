package com.future.function.web.dummy.data;

import com.future.function.common.data.core.UserData;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@OnlyStudentCanHaveBatchAndUniversity
public class DummyDataUser implements UserData {

  private String id;

  @Min(value = 10,
       message = "Min")
  private int number;

  @Email(message = "Email")
  @NotBlank(message = "NotBlank")
  private String email;

  private String role;

  private String batch;

  private String university;

}
