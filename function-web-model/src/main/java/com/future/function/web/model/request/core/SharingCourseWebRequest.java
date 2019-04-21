package com.future.function.web.model.request.core;

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
 * Model representation for course sharing web request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchesMustExist
@BatchesMustBeDistinct
public class SharingCourseWebRequest implements CourseData {
  
  @NotNull(message = "NotNull")
  private Long originBatch;
  
  @NotNull(message = "NotNull")
  private Long targetBatch;
  
  @Override
  public List<Long> getBatchNumbers() {
    
    return Arrays.asList(originBatch, targetBatch);
  }
  
}
