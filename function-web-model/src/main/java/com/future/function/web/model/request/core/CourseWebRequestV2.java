package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.FileMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Model representation for course web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseWebRequestV2 {
  
  @NotBlank(message = "NotBlank")
  private String title;
  
  @NotBlank(message = "NotBlank")
  private String description;
  
  @Size(max = 1,
        message = "Size")
  @FileMustExist
  private List<String> material;
  
}
