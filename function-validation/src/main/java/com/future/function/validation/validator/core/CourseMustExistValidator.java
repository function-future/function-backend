package com.future.function.validation.validator.core;

import com.future.function.common.data.core.SharedCourseData;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.repository.feature.core.SharedCourseRepository;
import com.future.function.validation.annotation.core.CourseMustExist;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseMustExistValidator
  implements ConstraintValidator<CourseMustExist, SharedCourseData> {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private SharedCourseRepository sharedCourseRepository;

  @Autowired
  private BatchRepository batchRepository;

  @Override
  public void initialize(CourseMustExist constraintAnnotation) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(
    SharedCourseData data, ConstraintValidatorContext context
  ) {

    return Optional.ofNullable(data.getOriginBatch())
      .map(originBatchCode -> this.doesSharedCoursesExistForBatch(
        data.getCourses(), originBatchCode))
      .orElseGet(() -> this.doesMasterCoursesExist(data.getCourses()));
  }

  private boolean doesMasterCoursesExist(List<String> courseIds) {

    return Optional.ofNullable(courseIds)
      .orElse(Collections.emptyList())
      .stream()
      .map(courseRepository::findByIdAndDeletedFalse)
      .allMatch(Optional::isPresent);
  }

  private boolean doesSharedCoursesExistForBatch(
    List<String> courseIds, String originBatchCode
  ) {

    Batch originBatch = this.getBatch(originBatchCode);

    if (originBatch == null) {
      return false;
    }

    return Optional.ofNullable(courseIds)
      .orElse(Collections.emptyList())
      .stream()
      .map(courseId -> sharedCourseRepository.findByIdAndBatch(courseId,
                                                               originBatch
      ))
      .allMatch(Optional::isPresent);
  }

  private Batch getBatch(String originBatchCode) {

    return batchRepository.findByCodeAndDeletedFalse(originBatchCode)
      .orElse(null);
  }

}
