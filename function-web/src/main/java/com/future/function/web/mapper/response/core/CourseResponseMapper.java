package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.File;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseResponseMapper {
  
  public static PagingResponse<CourseWebResponse> toCoursesPagingResponse(
    Page<Course> data
  ) {
    
    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, toCourseWebResponseList(data), PageHelper.toPaging(data));
  }
  
  private static List<CourseWebResponse> toCourseWebResponseList(
    Page<Course> data
  ) {
    
    return data.getContent()
      .stream()
      .map(CourseResponseMapper::buildCourseWebResponse)
      .collect(Collectors.toList());
  }
  
  public static DataResponse<CourseWebResponse> toCourseDataResponse(
    Course course
  ) {
    
    return toCourseDataResponse(HttpStatus.OK, course);
  }
  
  public static DataResponse<CourseWebResponse> toCourseDataResponse(
    HttpStatus httpStatus, Course course
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         buildCourseWebResponse(course)
    );
  }
  
  private static CourseWebResponse buildCourseWebResponse(Course course) {
    
    return CourseWebResponse.builder()
      .courseId(course.getId())
      .courseTitle(course.getTitle())
      .courseDescription(course.getDescription())
      .courseFileUrl(getFileUrl(course))
      .courseThumbnailUrl(getThumbnailUrl(course))
      .build();
  }
  
  private static String getFileUrl(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(File::getFileUrl)
      .orElse(null);
  }
  
  private static String getThumbnailUrl(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(File::getFileUrl)
      .orElse(null);
  }
  
}
