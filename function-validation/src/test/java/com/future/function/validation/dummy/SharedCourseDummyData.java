package com.future.function.validation.dummy;

import com.future.function.common.data.core.SharedCourseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedCourseDummyData implements SharedCourseData {

  private String originBatch;

  private String targetBatch;

  private List<String> courses;

}
