package com.future.function.repository.listener.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.SharedCourse;
import com.future.function.repository.feature.core.DiscussionRepository;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.repository.feature.core.SharedCourseRepository;
import com.future.function.repository.feature.core.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class BatchMongoEventListener extends AbstractMongoEventListener<Batch> {

  private final UserRepository userRepository;

  private final DiscussionRepository discussionRepository;

  private final FileRepositoryV2 fileRepository;

  private final SharedCourseRepository sharedCourseRepository;

  @Autowired
  public BatchMongoEventListener(
    UserRepository userRepository, DiscussionRepository discussionRepository,
    FileRepositoryV2 fileRepository,
    SharedCourseRepository sharedCourseRepository
  ) {

    this.userRepository = userRepository;
    this.discussionRepository = discussionRepository;
    this.fileRepository = fileRepository;
    this.sharedCourseRepository = sharedCourseRepository;
  }

  @Override
  public void onBeforeConvert(BeforeConvertEvent<Batch> event) {

    super.onBeforeConvert(event);

    Optional.of(event)
      .map(MongoMappingEvent::getSource)
      .filter(BaseEntity::isDeleted)
      .ifPresent(batch -> {
        this.markDeletedAllStudentOfBatch(batch);
        this.deleteSharedCoursesForBatch(batch);
      });
  }

  private void markDeletedAllStudentOfBatch(Batch batch) {

    userRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT, batch)
      .forEach(student -> {
        student.setDeleted(true);
        userRepository.save(student);
      });
  }

  private void deleteSharedCoursesForBatch(Batch batch) {

    sharedCourseRepository.findAllByBatch(batch)
      .forEach(sharedCourse -> {
        this.markCourseFilesUnused(sharedCourse);
        discussionRepository.deleteAllByCourseIdAndBatchId(
          sharedCourse.getId(), batch.getId());

        sharedCourseRepository.delete(sharedCourse);
      });
  }

  private void markCourseFilesUnused(SharedCourse sharedCourse) {

    Optional.of(sharedCourse)
      .map(SharedCourse::getCourse)
      .map(Course::getFile)
      .map(FileV2::getId)
      .map(Collections::singletonList)
      .ifPresent(this::markFilesUnused);
  }

  private void markFilesUnused(List<String> fileIds) {

    Optional.of(fileIds)
      .map(fileRepository::findAll)
      .map(Lists::newArrayList)
      .orElseGet(ArrayList::new)
      .forEach(fileV2 -> {
        fileV2.setUsed(false);
        fileRepository.save(fileV2);
      });
  }

}
