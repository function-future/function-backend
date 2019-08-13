package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SharedCourseService {
  
  Course getCourseByIdAndBatchCode(String courseId, String batchCode);
  
  Page<Course> getCoursesByBatchCode(String batchCode, Pageable pageable);
  
  void deleteCourseByIdAndBatchCode(String courseId, String batchCode);
  
  List<Course> createCourseForBatch(
    List<String> courseIds, String originBatchCode, String targetBatchCode
  );
  
  Course updateCourseForBatch(String courseId, String batchCode, Course course);
  
  Page<Discussion> getDiscussions(
    String email, String courseId, String batchCode, Pageable pageable
  );
  
  Discussion createDiscussion(
    Discussion discussion
  );
  
}
