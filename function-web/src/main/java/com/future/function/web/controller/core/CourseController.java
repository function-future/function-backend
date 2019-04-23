package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.sharing.SharingCourseService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.SharingCourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/core/courses")
public class CourseController {
  
  private final SharingCourseService sharingCourseService;
  
  private final CourseRequestMapper courseRequestMapper;
  
  @Autowired
  public CourseController(
    SharingCourseService sharingCourseService,
    CourseRequestMapper courseRequestMapper
  ) {
    
    this.sharingCourseService = sharingCourseService;
    this.courseRequestMapper = courseRequestMapper;
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
      sharingCourseService.getCourses(PageHelper.toPage(page, size), 1));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/_copy",
               produces = MediaType.APPLICATION_JSON_VALUE,
               consumes = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse copyCourses(
    @RequestBody
      SharingCourseWebRequest request
  ) {
    
    List<Long> batchNumbers = courseRequestMapper.toCopyCoursesData(request);
    sharingCourseService.copyCourses(batchNumbers.get(0), batchNumbers.get(1));
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }
  
}
