package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.CourseService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapperV2;
import com.future.function.web.mapper.response.core.CourseResponseMapperV2;
import com.future.function.web.model.request.core.CourseWebRequestV2;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponseV2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for course APIs.
 */
@RestController
@RequestMapping(value = "/api/core/courses")
public class CourseController {
  
  private final CourseService courseService;
  
  private final CourseRequestMapperV2 courseRequestMapperV2;
  
  public CourseController(
    CourseService courseService, CourseRequestMapperV2 courseRequestMapperV2
  ) {
    
    this.courseService = courseService;
    this.courseRequestMapperV2 = courseRequestMapperV2;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponseV2> getCourses(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size
  ) {
    
    return CourseResponseMapperV2.toCoursesPagingResponse(
      courseService.getCourses(PageHelper.toPageable(page, size)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}")
  public DataResponse<CourseWebResponseV2> getCourse(
    @PathVariable
      String courseId
  ) {
    
    return CourseResponseMapperV2.toCourseDataResponse(
      courseService.getCourse(courseId));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<CourseWebResponseV2> createCourse(
    @RequestBody
      CourseWebRequestV2 request
  ) {
    
    return CourseResponseMapperV2.toCourseDataResponse(
      HttpStatus.CREATED,
      courseService.createCourse(courseRequestMapperV2.toCourse(request))
    );
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{courseId}")
  public DataResponse<CourseWebResponseV2> updateCourse(
    @PathVariable
      String courseId,
    @RequestBody
      CourseWebRequestV2 request
  ) {
    
    return CourseResponseMapperV2.toCourseDataResponse(courseService.updateCourse(
      courseRequestMapperV2.toCourse(courseId, request)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{courseId}")
  public BaseResponse deleteCourse(
    @PathVariable
      String courseId
  ) {
    
    courseService.deleteCourse(courseId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
