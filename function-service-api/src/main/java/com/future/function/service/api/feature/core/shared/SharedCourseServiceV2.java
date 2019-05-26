package com.future.function.service.api.feature.core.shared;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SharedCourseServiceV2 {
  
  Course getCourseByIdAndBatchCode(String courseId, String batchCode);
  
  Page<Course> getCoursesByBatchCode(String batchCode, Pageable pageable);
  
  void deleteCourseByIdAndBatchCode(String courseId, String batchCode);
  
  List<Course> createCourseForBatch(
    List<String> courseIds, String originBatchCode, String targetBatchCode
  );
  
  Course updateCourseForBatch(String courseId, String batchCode, Course course);
  
}
