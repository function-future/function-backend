package com.future.function.web.model.request.core.shared;

import com.future.function.common.data.core.CourseData;
import com.future.function.validation.annotation.core.BatchesMustBeDistinct;
import com.future.function.validation.annotation.core.BatchesMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Model representation for course shared web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchesMustExist
@BatchesMustBeDistinct
public class SharedCourseWebRequest implements CourseData {
  
  @NotNull(message = "NotNull")
  private String originBatch;
  
  @NotNull(message = "NotNull")
  private String targetBatch;
  
  @Override
  public List<String> getBatchCodes() {
    
    return Arrays.asList(originBatch, targetBatch);
  }
  
}
