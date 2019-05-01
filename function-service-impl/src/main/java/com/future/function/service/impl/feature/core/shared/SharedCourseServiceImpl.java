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
import org.springframework.data.util.Pair;
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
    
    return this.getSharedCourseOptional(courseId, batchNumber)
      .map(SharedCourse::getCourse)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
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
      .map(sharedCourses -> toCoursePage(sharedCourses, pageable))
      .orElseThrow(() -> new NotFoundException("Get Courses Not Found"));
  }
  
  private Page<Course> toCoursePage(
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
   * @param course       Course data of new course.
   * @param file         File to be attached to course. May be null.
   * @param batchNumbers Batch numbers of the new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course createCourse(
    Course course, MultipartFile file, List<Long> batchNumbers
  ) {
    
    return Optional.of(course)
      .map(c -> courseService.createCourse(c, file))
      .map(c -> createSharedCourses(c, batchNumbers))
      .map(this::saveSharedCoursesAndGetCourse)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Create Shared Course or Course Failed"));
  }
  
  private Pair<Course, List<SharedCourse>> createSharedCourses(
    Course course, List<Long> batchNumbers
  ) {
    
    List<SharedCourse> sharedCourses = batchNumbers.stream()
      .map(number -> this.buildSharedCourse(course, number))
      .collect(Collectors.toList());
    
    return Pair.of(course, sharedCourses);
  }
  
  private SharedCourse buildSharedCourse(Course course, long batchNumber) {
    
    return SharedCourse.builder()
      .course(course)
      .batch(batchService.getBatch(batchNumber))
      .build();
  }
  
  /**
   * {@inheritDoc}
   *
   * @param course       Course data of new course.
   * @param file         File to be attached to course. May be null.
   * @param batchNumbers Batch numbers of the new course.
   *
   * @return {@code Course} - The course object of the saved data.
   */
  @Override
  public Course updateCourse(
    Course course, MultipartFile file, List<Long> batchNumbers
  ) {
    
    return Optional.of(course)
      .map(c -> courseService.updateCourse(c, file))
      .map(c -> Pair.of(c, this.getBatchesDataAsPair(c.getId(), batchNumbers)))
      .map(this::updateSharedCoursesAndGetCourse)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Update Shared Course or Course Failed"));
  }
  
  private Pair<List<Batch>, List<Long>> getBatchesDataAsPair(
    String courseId, List<Long> batchNumbers
  ) {
    
    List<Long> batchNumbersInDatabase =
      sharedCourseRepository.findAllByCourseId(courseId)
        .map(SharedCourse::getBatch)
        .map(Batch::getNumber)
        .collect(Collectors.toList());
    
    List<Batch> toDeleteFromSharedCourseRepo = batchNumbersInDatabase.stream()
      .filter(number -> !batchNumbers.contains(number))
      .map(batchService::getBatch)
      .collect(Collectors.toList());
    
    List<Long> toAddToSharedCourseRepo = batchNumbers.stream()
      .filter(number -> batchNumbersInDatabase.stream()
        .noneMatch(n -> n.equals(number)))
      .collect(Collectors.toList());
    
    return Pair.of(toDeleteFromSharedCourseRepo, toAddToSharedCourseRepo);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId    Id of course to be deleted.
   * @param batchNumber Batch number of selected course.
   */
  @Override
  public void deleteCourse(String courseId, long batchNumber) {
    
    this.getSharedCourseOptional(courseId, batchNumber)
      .ifPresent(
        sharedCourse -> deleteSharedCourseAndCourse(sharedCourse, courseId));
  }
  
  private void deleteSharedCourseAndCourse(
    SharedCourse sharedCourse, String courseId
  ) {
    
    sharedCourseRepository.delete(sharedCourse);
    
    deleteCourseIfNoSharingFound(courseId);
  }
  
  private void deleteCourseIfNoSharingFound(String courseId) {
    
    Optional.of(courseId)
      .map(sharedCourseRepository::findAllByCourseId)
      .filter(stream -> stream.count() == 0)
      .ifPresent(ignored -> courseService.deleteCourse(courseId));
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
  
  private Optional<SharedCourse> getSharedCourseOptional(
    String courseId, long batchNumber
  ) {
    
    return Optional.of(batchNumber)
      .map(batchService::getBatch)
      .flatMap(batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId,
                                                                      batch
      ));
  }
  
  private Course updateSharedCoursesAndGetCourse(
    Pair<Course, Pair<List<Batch>, List<Long>>> pair
  ) {
    
    Course course = pair.getFirst();
    Pair<List<Batch>, List<Long>> batchDataPair = pair.getSecond();
    
    this.deleteSharedCoursesByBatches(course.getId(), batchDataPair.getFirst());
    
    return this.saveSharedCoursesAndGetCourse(
      this.createSharedCourses(course, batchDataPair.getSecond()));
  }
  
  private void deleteSharedCoursesByBatches(
    String courseId, List<Batch> batches
  ) {
    
    batches.forEach(
      batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId, batch)
        .ifPresent(sharedCourseRepository::delete));
  }
  
  private Course saveSharedCoursesAndGetCourse(
    Pair<Course, List<SharedCourse>> pair
  ) {
    
    sharedCourseRepository.save(pair.getSecond());
    
    return pair.getFirst();
  }
  
}
