package com.future.function.web.model.request.scoring;

import com.future.function.validation.annotation.scoring.StudentListMustExist;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportWebRequest {

  @NotBlank(message = "NotBlank")
  private String name;

  @NotBlank(message = "NotBlank")
  private String description;

  @NotNull(message = "NotNull")
  @Size(min = 2,
      max = 3,
      message = "Size")
  @StudentListMustExist
  private List<String> students;

}
