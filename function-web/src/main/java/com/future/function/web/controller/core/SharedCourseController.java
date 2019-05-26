package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.service.api.feature.core.shared.SharedCourseService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapperV1;
import com.future.function.web.mapper.response.core.CourseResponseMapperV2;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponseV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class for course APIs.
 */
@RestController
@RequestMapping(value = "/api/core/batches/{batchId}/coursess")
public class SharedCourseController {
  
  // TODO When security is enabled, add parameter `Principal` to each method
  //  parameter, and check if the `Principal` has batch number (possibly in
  //  his authorities) or not. Otherwise, use parameter for `long batch`.
  
  private final SharedCourseService sharedCourseService;
  
  private final CourseRequestMapperV1 courseRequestMapperV1;
  
  @Autowired
  public SharedCourseController(
    SharedCourseService sharedCourseService,
    CourseRequestMapperV1 courseRequestMapperV1
  ) {
    
    this.sharedCourseService = sharedCourseService;
    this.courseRequestMapperV1 = courseRequestMapperV1;
  }
  
  /**
   * Retrieves courses based on given parameters.
   *
   * @param page  Current page of data.
   * @param size  Size of data to be displayed per page.
   * @param batch Batch number of current user (student) OR selected batch
   *              number (admin/judge/mentor).
   *
   * @return {@code PagingResponse<CourseWebResponse>} - The retrieved
   * courses data, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<CourseWebResponseV2> getCourses(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size,
    @RequestParam
      String batch
  ) {
    
    return CourseResponseMapperV2.toCoursesPagingResponse(
      sharedCourseService.getCourses(PageHelper.toPageable(page, size), batch));
  }
  
  /**
   * Copies courses from a batch to another batch.
   *
   * @param request Request body containing origin batch and target batch.
   *
   * @return {@code BaseResponse} - Indicating successful copying.
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/_copy",
               produces = MediaType.APPLICATION_JSON_VALUE,
               consumes = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse copyCourses(
    @RequestBody
      SharedCourseWebRequest request
  ) {
    
    List<String> batchNumbers = courseRequestMapperV1.toCopyCoursesData(
      request);
    sharedCourseService.copyCourses(batchNumbers.get(0), batchNumbers.get(1));
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }
  
  /**
   * Creates new course in database.
   *
   * @param data Data of new course in JSON format.
   * @param file File of the new course.
   *
   * @return {@code DataResponse<CourseWebResponse>} - The created
   * course data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<CourseWebResponseV2> createCourse(
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile file
  ) {
    
    Pair<Course, List<String>> pair =
      courseRequestMapperV1.toCourseAndBatchNumbers(data);
    return CourseResponseMapperV2.toCourseDataResponse(
      HttpStatus.CREATED, sharedCourseService.createCourse(
        pair.getFirst(), file, pair.getSecond()));
  }
  
  /**
   * Updates existing course in database.
   *
   * @param courseId Id of to-be-updated course.
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
  public DataResponse<CourseWebResponseV2> updateCourse(
    @PathVariable
      String courseId,
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile file
  ) {
    
    Pair<Course, List<String>> pair =
      courseRequestMapperV1.toCourseAndBatchNumbers(courseId, data);
    return CourseResponseMapperV2.toCourseDataResponse(
      sharedCourseService.updateCourse(pair.getFirst(), file,
                                       pair.getSecond()
      ));
  }
  
  /**
   * Retrieves a course based on given parameter.
   *
   * @param courseId Id of course to be retrieved.
   * @param batch    Batch number of current user (student) OR selected batch
   *                 number (admin/judge/mentor).
   *
   * @return {@code DataResponse<CourseWebResponse>} - The retrieved
   * course data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.CourseWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{courseId}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<CourseWebResponseV2> getCourse(
    @PathVariable
      String courseId,
    @RequestParam
      String batch
  ) {
    
    return CourseResponseMapperV2.toCourseDataResponse(
      sharedCourseService.getCourse(courseId, batch));
  }
  
  /**
   * Deletes course from database.
   *
   * @param courseId Id of to be deleted course.
   * @param batch    Batch number of of selected course.
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
      String batch
  ) {
    
    sharedCourseService.deleteCourse(courseId, batch);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
