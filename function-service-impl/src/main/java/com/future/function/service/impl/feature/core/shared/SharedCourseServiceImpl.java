package com.future.function.service.impl.feature.core.shared;

import com.future.function.common.exception.NotFoundException;
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
  
  @Override
  public Course getCourse(String courseId, long batchNumber) {
  
    return Optional.of(batchNumber)
      .map(batchService::getBatch)
      .flatMap(
        batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId, batch))
      .map(SharedCourse::getCourse)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }
  
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
  
  @Override
  public Course createCourse(
    Course course, MultipartFile file, long batchNumber
  ) {
  
    return Optional.of(course)
      .map(c -> courseService.createCourse(c, file))
      .map(c -> this.buildSharedCourse(batchNumber, c))
      .map(sharedCourseRepository::save)
      .map(SharedCourse::getCourse)
      .orElseThrow(() -> new NotFoundException("Create Course Not Found"));
  }
  
  @Override
  public Course updateCourse(
    Course course, MultipartFile file, long batchNumber
  ) {
  
    return Optional.of(batchNumber)
      .map(batchService::getBatch)
      .flatMap(
        batch -> sharedCourseRepository.findByCourseIdAndBatch(course.getId(),
                                                               batch
        ))
      .filter(sharedCourse -> isBatchNumberMatch(sharedCourse, batchNumber))
      .map(sharedCourse -> this.updateSharedCourseBatchAndSaveSharedCourse(
        sharedCourse, batchNumber))
      .map(SharedCourse::getCourse)
      .map(c -> courseService.updateCourse(c, file))
      .orElseThrow(() -> new NotFoundException("Update Course Not Found"));
  }
  
  private boolean isBatchNumberMatch(
    SharedCourse sharedCourse, long batchNumber
  ) {
    
    return sharedCourse.getBatch()
             .getNumber() == batchNumber;
  }
  
  private SharedCourse updateSharedCourseBatchAndSaveSharedCourse(
    SharedCourse sharedCourse, long batchNumber
  ) {
    
    sharedCourse.setBatch(batchService.getBatch(batchNumber));
    return sharedCourseRepository.save(sharedCourse);
  }
  
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
  
  private SharedCourse toSharedCourse(
    SharedCourse sharedCourse, long targetBatchNumber
  ) {
    
    return buildSharedCourse(targetBatchNumber, sharedCourse.getCourse());
  }
  
  private SharedCourse buildSharedCourse(
    long batchNumber, Course createdCourse
  ) {
    
    return SharedCourse.builder()
      .course(createdCourse)
      .batch(batchService.getBatch(batchNumber))
      .build();
  }
  
}
