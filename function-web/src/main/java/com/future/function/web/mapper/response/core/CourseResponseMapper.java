package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
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
public final class CourseResponseMapper {
  
  /**
   * Converts a course data to {@code CourseWebResponse}, wrapped in {@code
   * DataResponse}.
   *
   * @param course Course data to be converted to response.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The converted course
   * data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link CourseWebResponse}
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
   * {@link CourseWebResponse}
   */
  public static DataResponse<CourseWebResponse> toCourseDataResponse(
    HttpStatus httpStatus, Course course
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         buildNormalCourseWebResponseV2(course)
    );
  }
  
  private static CourseWebResponse buildNormalCourseWebResponseV2(
    Course course
  ) {
    
    return CourseWebResponse.builder()
      .id(course.getId())
      .title(course.getTitle())
      .description(course.getDescription())
      .material(CourseResponseMapper.getFileUrl(course))
      .build();
  }
  
  private static String getFileUrl(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getFileUrl)
      .orElse(null);
  }
  
  private static CourseWebResponse buildThumbnailCourseWebResponseV2(
    Course course
  ) {
    
    return CourseWebResponse.builder()
      .id(course.getId())
      .title(course.getTitle())
      .description(course.getDescription())
      .material(CourseResponseMapper.getMaterial(course))
      .build();
  }
  
  private static String getMaterial(Course course) {
    
    return Optional.ofNullable(CourseResponseMapper.getThumbnailUrl(course))
      .orElseGet(() -> CourseResponseMapper.getFileUrl(course));
  }
  
  private static String getThumbnailUrl(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getThumbnailUrl)
      .orElse(null);
  }
  
  /**
   * Converts courses data to {@code List<CourseWebResponse>}, wrapped in {@code
   * DataResponse}.
   *
   * @param courses Course data to be converted to response.
   *
   * @return {@code DataResponse<List<CourseWebResponse>>} - The converted
   * course data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link List} and {@link CourseWebResponse}.
   */
  public static DataResponse<List<CourseWebResponse>> toCoursesDataResponse(
    List<Course> courses
  ) {
    
    return ResponseHelper.toDataResponse(
      HttpStatus.CREATED, toCourseWebResponseV2List(courses));
  }
  
  private static List<CourseWebResponse> toCourseWebResponseV2List(
    List<Course> data
  ) {
    
    return data.stream()
      .map(CourseResponseMapper::buildThumbnailCourseWebResponseV2)
      .collect(Collectors.toList());
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
   * {@link CourseWebResponse}
   */
  public static PagingResponse<CourseWebResponse> toCoursesPagingResponse(
    Page<Course> data
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toCourseWebResponseV2List(data),
                                           PageHelper.toPaging(data)
    );
  }
  
  private static List<CourseWebResponse> toCourseWebResponseV2List(
    Page<Course> data
  ) {
    
    return toCourseWebResponseV2List(data.getContent());
  }
  
}
