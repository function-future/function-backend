package com.future.function.service.impl.feature.core.shared;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.shared.SharedCourse;
import com.future.function.repository.feature.core.shared.SharedCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.service.api.feature.core.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SharedCourseServiceImplV2 {
  
  private final SharedCourseRepository sharedCourseRepository;
  
  private final BatchService batchService;
  
  private final ResourceService resourceService;
  
  private final CourseService courseService;
  
  @Autowired
  public SharedCourseServiceImplV2(
    SharedCourseRepository sharedCourseRepository, BatchService batchService,
    ResourceService resourceService, CourseService courseService
  ) {
    
    this.sharedCourseRepository = sharedCourseRepository;
    this.batchService = batchService;
    this.resourceService = resourceService;
    this.courseService = courseService;
  }
  
  public Course getCourseByIdAndBatchCode(String courseId, String batchCode) {
    
    return this.getBatch(batchCode)
      .flatMap(
        batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId, batch))
      .map(SharedCourse::getCourse)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }
  
  private Optional<Batch> getBatch(String batchCode) {
    
    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode);
  }
  
  public Page<Course> getCoursesByBatchCode(
    String batchCode, Pageable pageable
  ) {
    
    return this.getBatch(batchCode)
      .map(batch -> sharedCourseRepository.findAllByBatch(batch, pageable))
      .map(sharedCourses -> this.toCoursePage(sharedCourses, pageable))
      .orElseGet(() -> new PageImpl<>(Collections.emptyList(), pageable, 0));
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
  
  public void deleteCourseByIdAndBatchCode(String courseId, String batchCode) {
    
    this.getBatch(batchCode)
      .flatMap(
        batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId, batch))
      .ifPresent(sharedCourse -> {
        Optional.of(sharedCourse)
          .map(SharedCourse::getCourse)
          .map(Course::getFile)
          .map(FileV2::getId)
          .map(Collections::singletonList)
          .ifPresent(fileIds -> resourceService.markFilesUsed(fileIds, false));
        
        sharedCourseRepository.delete(sharedCourse);
      });
  }
  
  public List<Course> createCourseForBatch(
    List<String> courseIds, String batchCode
  ) {
    
    return courseIds.stream()
      .map(courseService::getCourse)
      .map(course -> Pair.of(course, batchService.getBatchByCode(batchCode)))
      .map(courseAndBatchPair -> this.buildSharedCourse(
        courseAndBatchPair.getFirst(), courseAndBatchPair.getSecond()))
      .map(sharedCourseRepository::save)
      .map(SharedCourse::getCourse)
      .collect(Collectors.toList());
  }
  
  private SharedCourse buildSharedCourse(Course course, Batch batch) {
    
    return SharedCourse.builder()
      .course(course)
      .batch(batch)
      .build();
  }
  
  public Course updateCourseForBatch(
    String courseId, String batchCode, Course course
  ) {
    
    return this.getBatch(batchCode)
      .flatMap(
        batch -> sharedCourseRepository.findByCourseIdAndBatch(courseId, batch))
      .map(this::deleteCourseFile)
      .map(sharedCourse -> this.copyPropertiesAndSaveSharedCourse(sharedCourse,
                                                                  course
      ))
      .map(SharedCourse::getCourse)
      .orElse(course);
  }
  
  private SharedCourse copyPropertiesAndSaveSharedCourse(
    SharedCourse sharedCourse, Course course
  ) {
    
    resourceService.markFilesUsed(Collections.singletonList(course.getFile()
                                                              .getId()), true);
    course.setFile(resourceService.getFile(course.getFile()
                                             .getId()));
    
    BeanUtils.copyProperties(course, sharedCourse.getCourse());
    
    return sharedCourseRepository.save(sharedCourse);
  }
  
  private SharedCourse deleteCourseFile(SharedCourse sharedCourse) {
    
    return Optional.of(sharedCourse)
      .map(SharedCourse::getCourse)
      .map(Course::getFile)
      .map(FileV2::getId)
      .map(fileId -> resourceService.markFilesUsed(
        Collections.singletonList(fileId), false))
      .map(ignored -> sharedCourse)
      .orElse(sharedCourse);
  }
  
}
