package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.BatchMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedCourseWebRequest {
  
  @BatchMustExist
  private String originBatch;
  
  private List<String> courses;
  
}
