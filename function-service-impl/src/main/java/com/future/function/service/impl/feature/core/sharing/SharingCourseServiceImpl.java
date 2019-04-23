package com.future.function.service.impl.feature.core.sharing;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.sharing.SharingCourse;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.repository.feature.core.sharing.SharingCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.sharing.SharingCourseService;
import com.future.function.service.impl.feature.core.CourseServiceImpl;
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
public class SharingCourseServiceImpl extends CourseServiceImpl
  implements SharingCourseService {
  
  private final SharingCourseRepository sharingCourseRepository;
  
  private final BatchService batchService;
  
  @Autowired
  public SharingCourseServiceImpl(
    CourseRepository courseRepository,
    SharingCourseRepository sharingCourseRepository, BatchService batchService
  ) {
    
    super(courseRepository);
    this.sharingCourseRepository = sharingCourseRepository;
    this.batchService = batchService;
  }
  
  @Override
  public Course getCourse(String courseId, long batchNumber) {
    
    return sharingCourseRepository.findByCourseIdAndBatchNumber(
      courseId, batchNumber)
      .map(SharingCourse::getCourse)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }
  
  @Override
  public Page<Course> getCourses(Pageable pageable, long batchNumber) {
    
    return Optional.of(batchService.getBatch(batchNumber))
      .map(batch -> sharingCourseRepository.findAllByBatch(batch, pageable))
      .map(sharedCourses -> toPageCourse(sharedCourses, pageable))
      .orElseThrow(() -> new NotFoundException("Get Courses Not Found"));
  }
  
  private Page<Course> toPageCourse(
    Page<SharingCourse> sharingCourses, Pageable pageable
  ) {
    
    List<Course> courses = toCourseList(sharingCourses);
    
    return new PageImpl<>(courses, pageable, sharingCourses.getTotalElements());
  }
  
  private List<Course> toCourseList(Page<SharingCourse> sharingCourses) {
    
    return sharingCourses.getContent()
      .stream()
      .map(SharingCourse::getCourse)
      .collect(Collectors.toList());
  }
  
  @Override
  public void copyCourses(long originBatchNumber, long targetBatchNumber) {
    
    Stream<SharingCourse> originBatchSharedCourses =
      sharingCourseRepository.findAllByBatch(
        batchService.getBatch(originBatchNumber));
    
    List<SharingCourse> targetBatchSharedCourses = originBatchSharedCourses.map(
      sharedCourse -> toDeepCopySharingCourse(targetBatchNumber, sharedCourse))
      .collect(Collectors.toList());
    
    sharingCourseRepository.save(targetBatchSharedCourses);
  }
  
  @Override
  public Course createCourse(
    Course course, MultipartFile file, long batchNumber
  ) {
    
    Course createdCourse = super.createCourse(course, file);
    
    SharingCourse sharingCourse = SharingCourse.builder()
      .course(createdCourse)
      .batch(batchService.getBatch(batchNumber))
      .build();
    sharingCourseRepository.save(sharingCourse);
    
    return createdCourse;
  }
  
  @Override
  public Course updateCourse(
    Course course, MultipartFile file, long batchNumber
  ) {
    
    return null;
  }
  
  private SharingCourse toDeepCopySharingCourse(
    long targetBatch, SharingCourse sharingCourse
  ) {
    
    return SharingCourse.builder()
      .course(sharingCourse.getCourse())
      .batch(batchService.getBatch(targetBatch))
      .build();
  }
  
}
