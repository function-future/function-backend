package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.SharedCourseService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.SharedCourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.SharedCourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for course APIs.
 */
@RestController
@RequestMapping(value = "/api/core/batches/{batchCode}/courses")
public class SharedCourseController {
  
  private final SharedCourseService sharedCourseService;
  
  private final SharedCourseRequestMapper sharedCourseRequestMapper;
  
  @Autowired
  public SharedCourseController(
    SharedCourseService sharedCourseService,
    SharedCourseRequestMapper sharedCourseRequestMapper
  ) {
    
    this.sharedCourseService = sharedCourseService;
    this.sharedCourseRequestMapper = sharedCourseRequestMapper;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponse> getCoursesForBatch(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size,
    @PathVariable
      String batchCode
  ) {
    
    return CourseResponseMapper.toCoursesPagingResponse(
      sharedCourseService.getCoursesByBatchCode(batchCode,
                                                PageHelper.toPageable(page,
                                                                      size
                                                )
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}")
  public DataResponse<CourseWebResponse> getCourseForBatch(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @PathVariable
      String courseId,
    @PathVariable
      String batchCode
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      sharedCourseService.getCourseByIdAndBatchCode(courseId, batchCode));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{courseId}")
  public BaseResponse deleteCourseForBatch(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
      Session session,
    @PathVariable
      String courseId,
    @PathVariable
      String batchCode
  ) {
    
    sharedCourseService.deleteCourseByIdAndBatchCode(courseId, batchCode);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<List<CourseWebResponse>> createCourseForBatch(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
      Session session,
    @PathVariable
      String batchCode,
    @RequestBody
      SharedCourseWebRequest request
  ) {
    
    request.setTargetBatch(batchCode);
    Pair<List<String>, String> courseIdsAndOriginBatchPair =
      sharedCourseRequestMapper.toCourseIdsAndOriginBatchCodePair(request);
    
    return CourseResponseMapper.toCoursesDataResponse(
      sharedCourseService.createCourseForBatch(
        courseIdsAndOriginBatchPair.getFirst(),
        courseIdsAndOriginBatchPair.getSecond(), batchCode
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{courseId}")
  public DataResponse<CourseWebResponse> updateCourseForBatch(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
      Session session,
    @PathVariable
      String courseId,
    @PathVariable
      String batchCode,
    @RequestBody
      CourseWebRequest request
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      sharedCourseService.updateCourseForBatch(courseId, batchCode,
                                               sharedCourseRequestMapper.toCourse(
                                                 courseId, request)
      ));
  }
  
}
