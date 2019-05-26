package com.future.function.web.controller.core.shared;

import com.future.function.service.api.feature.core.shared.SharedCourseServiceV2;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapperV2;
import com.future.function.web.mapper.response.core.CourseResponseMapperV2;
import com.future.function.web.model.request.core.CourseWebRequestV2;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequestV2;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponseV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for course APIs.
 */
@RestController
@RequestMapping(value = "/api/core/batches/{batchCode}/courses")
public class SharedCourseControllerV2 {
  
  private final SharedCourseServiceV2 sharedCourseServiceV2;
  
  private final CourseRequestMapperV2 courseRequestMapperV2;
  
  @Autowired
  public SharedCourseControllerV2(
    SharedCourseServiceV2 sharedCourseServiceV2,
    CourseRequestMapperV2 courseRequestMapperV2
  ) {
    
    this.sharedCourseServiceV2 = sharedCourseServiceV2;
    this.courseRequestMapperV2 = courseRequestMapperV2;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponseV2> getCoursesForBatch(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size,
    @PathVariable
      String batchCode
  ) {
    
    return CourseResponseMapperV2.toCoursesPagingResponse(
      sharedCourseServiceV2.getCoursesByBatchCode(batchCode,
                                                  PageHelper.toPageable(page,
                                                                        size
                                                  )
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}")
  public DataResponse<CourseWebResponseV2> getCourseForBatch(
    @PathVariable
      String courseId,
    @PathVariable
      String batchCode
  ) {
    
    return CourseResponseMapperV2.toCourseDataResponse(
      sharedCourseServiceV2.getCourseByIdAndBatchCode(courseId, batchCode));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{courseId}")
  public BaseResponse deleteCourseForBatch(
    @PathVariable
      String courseId,
    @PathVariable
      String batchCode
  ) {
    
    sharedCourseServiceV2.deleteCourseByIdAndBatchCode(courseId, batchCode);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<List<CourseWebResponseV2>> createCourseForBatch(
    @PathVariable
      String batchCode,
    @RequestBody
      SharedCourseWebRequestV2 request
  ) {
    
    return CourseResponseMapperV2.toCoursesDataResponse(
      sharedCourseServiceV2.createCourseForBatch(request.getCourses(),
                                                 request.getOriginBatch(),
                                                 batchCode
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{courseId}")
  public DataResponse<CourseWebResponseV2> updateCourseForBatch(
    @PathVariable
      String courseId,
    @PathVariable
      String batchCode,
    @RequestBody
      CourseWebRequestV2 request
  ) {
    
    return CourseResponseMapperV2.toCourseDataResponse(
      sharedCourseServiceV2.updateCourseForBatch(courseId, batchCode,
                                                 courseRequestMapperV2.toCourse(
                                                   request)
      ));
  }
  
}
