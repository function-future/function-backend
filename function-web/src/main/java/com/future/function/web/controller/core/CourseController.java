package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.session.annotation.WithAnyRole;
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

/**
 * Controller class for course APIs.
 */
@RestController
@RequestMapping(value = "/api/core/courses")
public class CourseController {
  
  private final CourseService courseService;
  
  private final CourseRequestMapper courseRequestMapper;
  
  public CourseController(
    CourseService courseService, CourseRequestMapper courseRequestMapper
  ) {
    
    this.courseService = courseService;
    this.courseRequestMapper = courseRequestMapper;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
  public PagingResponse<CourseWebResponse> getCourses(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size
  ) {
    
    return CourseResponseMapper.toCoursesPagingResponse(
      courseService.getCourses(PageHelper.toPageable(page, size)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}")
  @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
  public DataResponse<CourseWebResponse> getCourse(
    @PathVariable
      String courseId
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      courseService.getCourse(courseId));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
  public DataResponse<CourseWebResponse> createCourse(
    @RequestBody
      CourseWebRequest request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      HttpStatus.CREATED,
      courseService.createCourse(courseRequestMapper.toCourse(request))
    );
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{courseId}")
  @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
  public DataResponse<CourseWebResponse> updateCourse(
    @PathVariable
      String courseId,
    @RequestBody
      CourseWebRequest request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(courseService.updateCourse(
      courseRequestMapper.toCourse(courseId, request)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{courseId}")
  @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
  public BaseResponse deleteCourse(
    @PathVariable
      String courseId
  ) {
    
    courseService.deleteCourse(courseId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
