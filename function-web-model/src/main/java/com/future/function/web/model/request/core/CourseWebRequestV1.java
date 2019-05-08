package com.future.function.web.model.request.core;

import com.future.function.common.data.core.CourseData;
import com.future.function.validation.annotation.core.BatchesMustBeDistinct;
import com.future.function.validation.annotation.core.BatchesMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @deprecated Model representation for course web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchesMustExist
@BatchesMustBeDistinct
public class CourseWebRequestV1 implements CourseData {
  
  @NotBlank(message = "NotBlank")
  private String courseTitle;
  
  @NotBlank(message = "NotBlank")
  private String courseDescription;
  
  @NotEmpty(message = "NotEmpty")
  private List<Long> batchNumbers;
  
}
