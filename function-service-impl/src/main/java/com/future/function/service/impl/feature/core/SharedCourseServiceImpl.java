package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.SharedCourse;
import com.future.function.repository.feature.core.SharedCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.service.api.feature.core.DiscussionService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.SharedCourseService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SharedCourseServiceImpl implements SharedCourseService {

  private final SharedCourseRepository sharedCourseRepository;

  private final BatchService batchService;

  private final ResourceService resourceService;

  private final CourseService courseService;

  private final DiscussionService discussionService;

  @Autowired
  public SharedCourseServiceImpl(
    SharedCourseRepository sharedCourseRepository, BatchService batchService,
    ResourceService resourceService, CourseService courseService,
    DiscussionService discussionService
  ) {

    this.sharedCourseRepository = sharedCourseRepository;
    this.batchService = batchService;
    this.resourceService = resourceService;
    this.courseService = courseService;
    this.discussionService = discussionService;
  }

  private Course setCourseId(SharedCourse sharedCourse) {

    Course course = sharedCourse.getCourse();
    course.setId(sharedCourse.getId());

    return course;
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

  @Override
  public Course getCourseByIdAndBatchCode(
    Session session, String courseId, String batchCode
  ) {

    return this.getBatch(batchCode)
      .filter(batch -> this.isUserAccessingProperSharedCourse(session, batch))
      .flatMap(
        batch -> sharedCourseRepository.findByIdAndBatch(courseId, batch))
      .map(this::setCourseId)
      .orElseThrow(() -> new NotFoundException("Get Course Not Found"));
  }


  private Optional<Batch> getBatch(String batchCode) {

    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode);
  }

  @Override
  public Page<Course> getCoursesByBatchCode(
    Session session, String batchCode, Pageable pageable
  ) {

    return this.getBatch(batchCode)
      .filter(batch -> this.isUserAccessingProperSharedCourse(session, batch))
      .map(batch -> sharedCourseRepository.findAllByBatch(batch, pageable))
      .map(sharedCourses -> this.toCoursePage(sharedCourses, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  private boolean isUserAccessingProperSharedCourse(
    Session session, Batch batch
  ) {

    return Optional.of(session)
      .map(Session::getBatchId)
      .map(userBatchId -> this.isStudentAccessingProperSharedCourse(batch,
                                                                    userBatchId
      ))
      .orElse(Boolean.TRUE);
  }

  private boolean isStudentAccessingProperSharedCourse(
    Batch batch, String studentBatchId
  ) {

    return Optional.of(studentBatchId)
      .map(batch.getId()::equals)
      .filter(Boolean::booleanValue)
      .orElseThrow(() -> new ForbiddenException(
        "Invalid Student Batch for Accessing Shared Course"));
  }

  private Page<Course> toCoursePage(
    Page<SharedCourse> sharedCourses, Pageable pageable
  ) {

    List<Course> courses = toCourseList(sharedCourses);

    return PageHelper.toPage(courses, pageable);
  }

  private List<Course> toCourseList(Page<SharedCourse> sharedCourses) {

    return sharedCourses.getContent()
      .stream()
      .map(this::setCourseId)
      .collect(Collectors.toList());
  }

  @Override
  public void deleteCourseByIdAndBatchCode(String courseId, String batchCode) {

    this.getBatch(batchCode)
      .flatMap(
        batch -> sharedCourseRepository.findByIdAndBatch(courseId, batch))
      .ifPresent(sharedCourse -> {
        this.markCourseFilesUnused(sharedCourse);
        this.deleteDiscussionsForSharedCourse(sharedCourse);
        sharedCourseRepository.delete(sharedCourse);
      });
  }

  private void deleteDiscussionsForSharedCourse(SharedCourse sharedCourse) {

    discussionService.deleteDiscussions(
      sharedCourse.getId(), sharedCourse.getBatch()
        .getCode());
  }

  private void markCourseFilesUnused(SharedCourse sharedCourse) {

    Optional.of(sharedCourse)
      .map(SharedCourse::getCourse)
      .map(Course::getFile)
      .map(FileV2::getId)
      .map(Collections::singletonList)
      .ifPresent(fileIds -> resourceService.markFilesUsed(fileIds, false));
  }

  @Override
  public List<Course> createCourseForBatch(
    List<String> courseIds, String originBatchCode, String targetBatchCode
  ) {

    return Optional.ofNullable(originBatchCode)
      .filter(code -> !StringUtils.isEmpty(code))
      .map(code -> this.createCourseForBatchFromAnotherBatch(courseIds,
                                                             originBatchCode,
                                                             targetBatchCode
      ))
      .orElseGet(() -> this.createCourseForBatchFromMasterData(courseIds,
                                                               targetBatchCode
      ));
  }

  private List<Course> createCourseForBatchFromAnotherBatch(
    List<String> sharedCourseIds, String originBatchCode, String targetBatchCode
  ) {

    Batch originBatch = batchService.getBatchByCode(originBatchCode);
    Batch targetBatch = batchService.getBatchByCode(targetBatchCode);

    return sharedCourseRepository.findAllByBatch(originBatch)
      .filter(sharedCourse -> sharedCourseIds.contains(sharedCourse.getId()))
      .map(SharedCourse::getCourse)
      .map(this::createCourseFileCopy)
      .map(course -> this.buildSharedCourse(course, targetBatch))
      .map(sharedCourseRepository::save)
      .map(this::setCourseId)
      .collect(Collectors.toList());
  }

  private Course createCourseFileCopy(Course course) {

    Optional.of(course)
      .filter(c -> !new FileV2().equals(c.getFile()))
      .ifPresent(c -> c.setFile(
        resourceService.createACopy(course.getFile(), FileOrigin.COURSE)));

    return course;
  }

  private SharedCourse buildSharedCourse(Course course, Batch targetBatch) {

    return SharedCourse.builder()
      .course(course)
      .batch(targetBatch)
      .build();
  }

  private List<Course> createCourseForBatchFromMasterData(
    List<String> courseIds, String batchCode
  ) {

    return courseIds.stream()
      .map(courseService::getCourse)
      .map(this::createCourseFileCopy)
      .map(course -> Pair.of(course, batchService.getBatchByCode(batchCode)))
      .map(courseAndBatchPair -> this.buildSharedCourse(
        courseAndBatchPair.getFirst(), courseAndBatchPair.getSecond()))
      .map(sharedCourseRepository::save)
      .map(this::setCourseId)
      .collect(Collectors.toList());
  }

  @Override
  public Course updateCourseForBatch(
    String courseId, String batchCode, Course course
  ) {

    return this.getBatch(batchCode)
      .flatMap(
        batch -> sharedCourseRepository.findByIdAndBatch(courseId, batch))
      .map(this::deleteCourseFile)
      .map(sharedCourse -> this.copyPropertiesAndSaveSharedCourse(sharedCourse,
                                                                  course
      ))
      .map(this::setCourseId)
      .orElse(course);
  }

  @Override
  public Page<Discussion> getDiscussions(
    Session session, String courseId, String batchCode, Pageable pageable
  ) {

    Optional.of(session.getEmail())
      .ifPresent(ignored -> this.getCourseByIdAndBatchCode(session, courseId,
                                                           batchCode
      ));

    return discussionService.getDiscussions(
      session.getEmail(), courseId, batchCode, pageable);
  }

  @Override
  public Discussion createDiscussion(Session session, Discussion discussion) {

    Optional.of(discussion)
      .ifPresent(d -> this.getCourseByIdAndBatchCode(session, d.getCourseId(),
                                                     d.getBatchCode()
      ));

    return discussionService.createDiscussion(discussion);
  }

  private SharedCourse copyPropertiesAndSaveSharedCourse(
    SharedCourse sharedCourse, Course course
  ) {

    Optional.ofNullable(course)
      .map(Course::getFile)
      .map(FileV2::getId)
      .ifPresent(fileId -> {
        resourceService.markFilesUsed(Collections.singletonList(fileId), true);
        course.setFile(resourceService.getFile(fileId));
      });

    CopyHelper.copyProperties(course, sharedCourse.getCourse());

    return sharedCourseRepository.save(sharedCourse);
  }

}
