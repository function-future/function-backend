package com.future.function.web.model.request.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.function.common.data.core.SharedCourseData;
import com.future.function.validation.annotation.core.BatchCodesMustBeDistinct;
import com.future.function.validation.annotation.core.BatchMustExist;
import com.future.function.validation.annotation.core.CourseMustBeDistinct;
import com.future.function.validation.annotation.core.CourseMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CourseMustExist
@BatchCodesMustBeDistinct
public class SharedCourseWebRequest implements SharedCourseData {

  @BatchMustExist(field = "originBatch")
  private String originBatch;

  @JsonIgnore
  private String targetBatch;

  @CourseMustBeDistinct
  @NotEmpty(message = "NotEmpty")
  private List<String> courses;

}
