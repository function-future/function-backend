package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.CourseService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapperV2;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.CourseWebRequestV2;
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
  
  private final CourseRequestMapperV2 courseRequestMapperV2;
  
  public CourseController(
    CourseService courseService, CourseRequestMapperV2 courseRequestMapperV2
  ) {
    
    this.courseService = courseService;
    this.courseRequestMapperV2 = courseRequestMapperV2;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponse> getCourses(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size
  ) {
    
    return CourseResponseMapper.toCoursesPagingResponse(
      courseService.getCourses(PageHelper.toPage(page, size)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}")
  public DataResponse<CourseWebResponse> getCourse(
    @PathVariable
      String courseId
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      courseService.getCourse(courseId));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<CourseWebResponse> createCourse(
    @RequestBody
      CourseWebRequestV2 request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      HttpStatus.CREATED,
      courseService.createCourse(courseRequestMapperV2.toCourse(request))
    );
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{courseId}")
  public DataResponse<CourseWebResponse> updateCourse(
    @PathVariable
      String courseId,
    @RequestBody
      CourseWebRequestV2 request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(courseService.updateCourse(
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
