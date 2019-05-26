package com.future.function.web.model.request.core.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedCourseWebRequestV2 {

  private String originBatch;
  
  private List<String> courses;

}
