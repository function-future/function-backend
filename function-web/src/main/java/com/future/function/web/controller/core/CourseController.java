package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/core/courses")
@WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
public class CourseController {
  
  private final CourseService courseService;
  
  private final CourseRequestMapper courseRequestMapper;
  
  private final FileProperties fileProperties;
  
  public CourseController(
    CourseService courseService, CourseRequestMapper courseRequestMapper,
    FileProperties fileProperties
  ) {
    
    this.courseService = courseService;
    this.courseRequestMapper = courseRequestMapper;
    this.fileProperties = fileProperties;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponse> getCourses(
      Session session,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size
  ) {
    
    return CourseResponseMapper.toCoursesPagingResponse(
      courseService.getCourses(PageHelper.toPageable(page, size)),
      fileProperties.getUrlPrefix());
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}")
  public DataResponse<CourseWebResponse> getCourse(
      Session session,
    @PathVariable
      String courseId
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      courseService.getCourse(courseId), fileProperties.getUrlPrefix());
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<CourseWebResponse> createCourse(
      Session session,
    @RequestBody
      CourseWebRequest request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      HttpStatus.CREATED,
      courseService.createCourse(courseRequestMapper.toCourse(request)),
      fileProperties.getUrlPrefix()
    );
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{courseId}")
  public DataResponse<CourseWebResponse> updateCourse(
      Session session,
    @PathVariable
      String courseId,
    @RequestBody
      CourseWebRequest request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(courseService.updateCourse(
      courseRequestMapper.toCourse(courseId, request)), fileProperties.getUrlPrefix());
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{courseId}")
  public BaseResponse deleteCourse(
      Session session,
    @PathVariable
      String courseId
  ) {
    
    courseService.deleteCourse(courseId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
