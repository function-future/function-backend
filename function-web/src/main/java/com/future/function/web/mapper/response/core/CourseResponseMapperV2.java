package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponseV2;
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
public final class CourseResponseMapperV2 {
  
  /**
   * Converts a course data to {@code CourseWebResponseV2}, wrapped in {@code
   * DataResponse}.
   *
   * @param course Course data to be converted to response.
   *
   * @return {@code DataResponse<CourseWebResponseV2>} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponseV2}
   */
  public static DataResponse<CourseWebResponseV2> toCourseDataResponse(
    Course course
  ) {
    
    return toCourseDataResponse(HttpStatus.OK, course);
  }
  
  /**
   * Converts a course data to {@code CourseWebResponseV2} given {@code
   * HttpStatus}, wrapped in {@code DataResponse}.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param course     Course data to be converted to response.
   *
   * @return {@code DataResponse<CourseWebResponseV2>} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponseV2}
   */
  public static DataResponse<CourseWebResponseV2> toCourseDataResponse(
    HttpStatus httpStatus, Course course
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         buildNormalCourseWebResponseV2(course)
    );
  }
  
  private static CourseWebResponseV2 buildNormalCourseWebResponseV2(Course course) {
    
    return CourseWebResponseV2.builder()
      .id(course.getId())
      .title(course.getTitle())
      .description(course.getDescription())
      .material(CourseResponseMapperV2.getFileUrl(course))
      .build();
  }
  
  private static CourseWebResponseV2 buildThumbnailCourseWebResponseV2(Course course) {
    
    return CourseWebResponseV2.builder()
      .id(course.getId())
      .title(course.getTitle())
      .description(course.getDescription())
      .material(CourseResponseMapperV2.getThumbnailUrl(course))
      .build();
  }
  
  private static String getFileUrl(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getFileUrl)
      .orElse(null);
  }
  
  private static String getThumbnailUrl(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getThumbnailUrl)
      .orElse(null);
  }
  
  /**
   * Converts a course data to {@code CourseWebResponseV2}, wrapped in {@code
   * DataResponse}.
   *
   * @param courses Course data to be converted to response.
   *
   * @return {@code DataResponse<CourseWebResponseV2>} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponseV2}
   */
  public static DataResponse<List<CourseWebResponseV2>> toCoursesDataResponse(
    List<Course> courses
  ) {
    
    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toCourseWebResponseV2List(courses));
  }
  
  private static List<CourseWebResponseV2> toCourseWebResponseV2List(
    Page<Course> data
  ) {
    
    return toCourseWebResponseV2List(data.getContent());
  }
  
  private static List<CourseWebResponseV2> toCourseWebResponseV2List(
    List<Course> data
  ) {
    
    return data.stream()
      .map(CourseResponseMapperV2::buildThumbnailCourseWebResponseV2)
      .collect(Collectors.toList());
  }
  
  /**
   * Converts courses data to {@code CourseWebResponseV2} given {@code
   * HttpStatus}, wrapped in {@code PagingResponse}.
   *
   * @param data Courses data to be converted to response.
   *
   * @return {@code PagingResponse<CourseWebResponseV2} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponseV2}
   */
  public static PagingResponse<CourseWebResponseV2> toCoursesPagingResponse(
    Page<Course> data
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toCourseWebResponseV2List(data),
                                           PageHelper.toPaging(data)
    );
  }
  
}
