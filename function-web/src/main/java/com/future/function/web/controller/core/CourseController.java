package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.shared.SharedCourseService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class for course APIs.
 */
@RestController
@RequestMapping(value = "/api/core/courses")
public class CourseController {
  
  private final SharedCourseService sharedCourseService;
  
  private final CourseRequestMapper courseRequestMapper;
  
  @Autowired
  public CourseController(
    SharedCourseService sharedCourseService, CourseRequestMapper courseRequestMapper
  ) {
    
    this.sharedCourseService = sharedCourseService;
    this.courseRequestMapper = courseRequestMapper;
  }
  
  /**
   * Retrieves courses based on given parameters.
   *
   * @param page  Current page of data.
   * @param size  Size of data to be displayed per page.
   * @param batch Batch number of current user.
   *
   * @return {@code PagingResponse<CourseWebResponse>} - The retrieved
   * courses data, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponse> getCourses(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size,
    @RequestParam(defaultValue = "1")
      long batch
  ) {
    
    return CourseResponseMapper.toCoursesPagingResponse(
      sharedCourseService.getCourses(PageHelper.toPage(page, size), batch));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/_copy",
               produces = MediaType.APPLICATION_JSON_VALUE,
               consumes = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse copyCourses(
    @RequestBody
      SharedCourseWebRequest request
  ) {
    
    List<Long> batchNumbers = courseRequestMapper.toCopyCoursesData(request);
    sharedCourseService.copyCourses(batchNumbers.get(0), batchNumbers.get(1));
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }
  
  /**
   * Creates new course in database.
   *
   * @param batch Batch number of current user.
   * @param data  Data of new course in JSON format.
   * @param file  File of the new course.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The created
   * course data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<CourseWebResponse> createCourse(
    @RequestParam
      long batch,
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile file
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(HttpStatus.CREATED,
                                                     sharedCourseService.createCourse(
                                                       courseRequestMapper.toCourse(
                                                         data), file, batch)
    );
  }
  
  /**
   * Updates existing course in database.
   *
   * @param courseId Id of to-be-updated course.
   * @param batch    Batch number of current user.
   * @param data     Data of existing course in JSON format.
   * @param file     New file of the existing course.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The updated
   * course data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{courseId}",
              consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<CourseWebResponse> updateCourse(
    @PathVariable
      String courseId,
    @RequestParam
      long batch,
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile file
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(sharedCourseService.updateCourse(
      courseRequestMapper.toCourse(courseId, data), file, batch));
  }
  
  /**
   * Retrieves a course based on given parameter.
   *
   * @param courseId Id of course to be retrieved.
   * @param batch    Batch number of current user.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The retrieved
   * course data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<CourseWebResponse> getCourse(
    @PathVariable
      String courseId,
    @RequestParam
      long batch
  ) {
    
    return CourseResponseMapper.toCourseDataResponse(
      sharedCourseService.getCourse(courseId, batch));
  }
  
  /**
   * Deletes course from database.
   *
   * @param courseId Id of to be deleted course.
   * @param batch    Batch number of current user.
   *
   * @return {@code BaseResponse} - Indicating successful deletion.
   */
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{courseId}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteCourse(
    @PathVariable
      String courseId,
    @RequestParam
      long batch
  ) {
    
    sharedCourseService.deleteCourse(courseId, batch);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
