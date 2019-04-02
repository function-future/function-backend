package com.future.function.web.dummy;

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
public class DummyData {
  
  @Min(value = 10, message = "Min")
  private int number;
  
  @Email(message = "Email")
  @NotBlank(message = "NotBlank")
  private String email;

}
