package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.File;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper class for course web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseResponseMapper {
  
  /**
   * Converts a course data to {@code CourseWebResponse}, wrapped in {@code
   * DataResponse}.
   *
   * @param course Course data to be converted to response.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  public static DataResponse<CourseWebResponse> toCourseDataResponse(
    Course course
  ) {
    
    return toCourseDataResponse(HttpStatus.OK, course);
  }
  
  /**
   * Converts a course data to {@code CourseWebResponse} given {@code
   * HttpStatus}, wrapped in {@code DataResponse}.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param course     Course data to be converted to response.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
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
  
  /**
   * Converts courses data to {@code CourseWebResponse} given {@code
   * HttpStatus}, wrapped in {@code PagingResponse}.
   *
   * @param data Courses data to be converted to response.
   *
   * @return {@code PagingResponse<CourseWebResponse} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  public static PagingResponse<CourseWebResponse> toCoursesPagingResponse(
    Page<Course> data
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toCourseWebResponseList(data), PageHelper.toPaging(data));
  }
  
  private static List<CourseWebResponse> toCourseWebResponseList(
    Page<Course> data
  ) {
    
    return data.getContent()
      .stream()
      .map(CourseResponseMapper::buildCourseWebResponse)
      .collect(Collectors.toList());
  }
  
}
