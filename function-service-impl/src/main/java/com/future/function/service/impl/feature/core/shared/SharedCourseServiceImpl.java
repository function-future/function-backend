package com.future.function.service.impl.feature.core.shared;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.shared.SharedCourse;
import com.future.function.repository.feature.core.shared.SharedCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.service.api.feature.core.shared.SharedCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service implementation class for shared course logic operations
 * implementation.
 */
@Service
public class SharedCourseServiceImpl implements SharedCourseService {
  
  private final SharedCourseRepository sharedCourseRepository;
  
  private final BatchService batchService;
  
  private final CourseService courseService;
  
  @Autowired
  public SharedCourseServiceImpl(
    SharedCourseRepository sharedCourseRepository, BatchService batchService,
    CourseService courseService
  ) {
    
    this.sharedCourseRepository = sharedCourseRepository;
    this.batchService = batchService;
    this.courseService = courseService;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId    Id of course to be retrieved.
   * @param batchNumber Batch number of current user (obtained from session).
   *
   * @return {@code Course} - The course object found in database.
   */
  @Override
  public Course getCourse(String courseId, long batchNumber) {
    
    return getSharedCourse(courseId, batchNumber).map(SharedCourse::getCourse)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }
  
  private Optional<SharedCourse> getSharedCourse(
    String courseId, long batchNumber
  ) {
    
    return Optional.of(batchNumber)
      .map(batchService::getBatch)
      .flatMap(batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId,
                                                                      batch
      ));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param pageable    Pageable object for paging data.
   * @param batchNumber Batch number of current user (obtained from session).
   *
   * @return {@code Page<Course>} - Page of courses found in database.
   */
  @Override
  public Page<Course> getCourses(Pageable pageable, long batchNumber) {
    
    return Optional.of(batchNumber)
      .map(batchService::getBatch)
      .map(batch -> sharedCourseRepository.findAllByBatch(batch, pageable))
      .map(sharedCourses -> toPageCourse(sharedCourses, pageable))
      .orElseThrow(() -> new NotFoundException("Get Courses Not Found"));
  }
  
  private Page<Course> toPageCourse(
    Page<SharedCourse> sharedCourses, Pageable pageable
  ) {
    
    List<Course> courses = toCourseList(sharedCourses);
    return new PageImpl<>(courses, pageable, sharedCourses.getTotalElements());
  }
  
  private List<Course> toCourseList(Page<SharedCourse> sharedCourses) {
    
    return sharedCourses.getContent()
      .stream()
      .map(SharedCourse::getCourse)
      .collect(Collectors.toList());
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course      Course data of new course.
   * @param file        File to be attached to course. May be null.
   * @param batchNumber Batch number of current user (obtained from session).
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course createCourse(
    Course course, MultipartFile file, long batchNumber
  ) {
    
    return Optional.of(course)
      .map(c -> courseService.createCourse(c, file))
      .map(c -> this.buildSharedCourse(c, batchNumber))
      .map(this::saveSharedCourseAndGetCourse)
      .orElseThrow(() -> new NotFoundException("Create Course Not Found"));
  }
  
  private SharedCourse buildSharedCourse(Course course, long batchNumber) {
    
    return SharedCourse.builder()
      .course(course)
      .batch(batchService.getBatch(batchNumber))
      .build();
  }
  
  private Course saveSharedCourseAndGetCourse(SharedCourse sharedCourse) {
    
    SharedCourse savedSharedCourse = sharedCourseRepository.save(sharedCourse);
    return savedSharedCourse.getCourse();
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course      Course data of new course.
   * @param file        File to be attached to course. May be null.
   * @param batchNumber Batch number of current user (obtained from session).
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course updateCourse(
    Course course, MultipartFile file, long batchNumber
  ) {
    
    return getSharedCourse(course.getId(), batchNumber).filter(
      sharedCourse -> isBatchNumberMatch(sharedCourse.getBatch(), batchNumber))
      .map(sharedCourse -> this.updateSharedCourseBatchAndSaveSharedCourse(
        sharedCourse, batchNumber))
      .map(c -> courseService.updateCourse(c, file))
      .orElseThrow(() -> new NotFoundException("Update Course Not Found"));
  }
  
  private boolean isBatchNumberMatch(Batch batch, long batchNumber) {
    
    return batch.getNumber() == batchNumber;
  }
  
  private Course updateSharedCourseBatchAndSaveSharedCourse(
    SharedCourse sharedCourse, long batchNumber
  ) {
    
    sharedCourse.setBatch(batchService.getBatch(batchNumber));
    return this.saveSharedCourseAndGetCourse(sharedCourse);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId    Id of course to be deleted.
   * @param batchNumber Batch number of current user (obtained from session).
   */
  @Override
  public void deleteCourse(String courseId, long batchNumber) {
    
    Optional.ofNullable(courseId)
      .map(id -> batchNumber)
      .map(batchService::getBatch)
      .flatMap(
        batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId, batch))
      .ifPresent(
        sharedCourse -> deleteSharedCourseAndCourse(sharedCourse, courseId));
  }
  
  private void deleteSharedCourseAndCourse(
    SharedCourse sharedCourse, String courseId
  ) {
    
    sharedCourseRepository.delete(sharedCourse);
    courseService.deleteCourse(courseId);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param originBatchNumber Batch number of origin courses.
   * @param targetBatchNumber Targeted batch number for courses to be copied.
   */
  @Override
  public void copyCourses(long originBatchNumber, long targetBatchNumber) {
    
    Stream<SharedCourse> originBatchSharedCourses =
      sharedCourseRepository.findAllByBatch(
        batchService.getBatch(originBatchNumber));
    
    List<SharedCourse> targetBatchSharedCourses = originBatchSharedCourses.map(
      sharedCourse -> toSharedCourse(sharedCourse, targetBatchNumber))
      .collect(Collectors.toList());
    
    sharedCourseRepository.save(targetBatchSharedCourses);
  }
  
  private SharedCourse toSharedCourse(
    SharedCourse sharedCourse, long targetBatchNumber
  ) {
    
    return buildSharedCourse(sharedCourse.getCourse(), targetBatchNumber);
  }
  
}
