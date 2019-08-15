package com.future.function.validation.dummy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DummyData {

  @Min(value = 10,
       message = "Min")
  private int number;

  @NotBlank(message = "NotBlank")
  private String string;

}
