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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseResponseMapper {
  
  public static DataResponse<CourseWebResponse> toCourseDataResponse(
    Course course, String urlPrefix
  ) {
    
    return toCourseDataResponse(HttpStatus.OK, course, urlPrefix);
  }
  
  public static DataResponse<CourseWebResponse> toCourseDataResponse(
    HttpStatus httpStatus, Course course, String urlPrefix
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         buildNormalCourseWebResponseV2(course, urlPrefix)
    );
  }
  
  private static CourseWebResponse buildNormalCourseWebResponseV2(
    Course course, String urlPrefix
  ) {
    
    return CourseWebResponse.builder()
      .id(course.getId())
      .title(course.getTitle())
      .description(course.getDescription())
      .material(CourseResponseMapper.getFileUrl(course, urlPrefix))
      .materialId(CourseResponseMapper.getFileId(course))
      .build();
  }
  
  private static String getFileId(Course course) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getId)
      .orElse(null);
  }
  
  private static String getFileUrl(Course course, String urlPrefix) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getFileUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }
  
  private static CourseWebResponse buildThumbnailCourseWebResponseV2(
    Course course, String urlPrefix
  ) {
    
    return CourseWebResponse.builder()
      .id(course.getId())
      .title(course.getTitle())
      .description(course.getDescription())
      .material(CourseResponseMapper.getMaterial(course, urlPrefix))
      .build();
  }
  
  private static String getMaterial(Course course, String urlPrefix) {
    
    return Optional.ofNullable(CourseResponseMapper.getThumbnailUrl(course,
                                                                    urlPrefix))
      .orElseGet(() -> CourseResponseMapper.getFileUrl(course, urlPrefix));
  }
  
  private static String getThumbnailUrl(
    Course course, String urlPrefix
  ) {
    
    return Optional.ofNullable(course.getFile())
      .map(FileV2::getThumbnailUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }
  
  public static DataResponse<List<CourseWebResponse>> toCoursesDataResponse(
    List<Course> courses, String urlPrefix
  ) {
    
    return ResponseHelper.toDataResponse(
      HttpStatus.CREATED, toCourseWebResponseV2List(courses, urlPrefix));
  }
  
  private static List<CourseWebResponse> toCourseWebResponseV2List(
    List<Course> data, String urlPrefix
  ) {
    
    return data.stream()
      .map(course -> buildThumbnailCourseWebResponseV2(course, urlPrefix))
      .collect(Collectors.toList());
  }
  
  public static PagingResponse<CourseWebResponse> toCoursesPagingResponse(
    Page<Course> data, String urlPrefix
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toCourseWebResponseV2List(data, urlPrefix),
                                           PageHelper.toPaging(data)
    );
  }
  
  private static List<CourseWebResponse> toCourseWebResponseV2List(
    Page<Course> data, String urlPrefix
  ) {
    
    return toCourseWebResponseV2List(data.getContent(), urlPrefix);
  }
  
}
