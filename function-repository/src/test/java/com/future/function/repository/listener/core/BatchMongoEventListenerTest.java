package com.future.function.repository.listener.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.SharedCourse;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.repository.feature.core.DiscussionRepository;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.repository.feature.core.SharedCourseRepository;
import com.future.function.repository.feature.core.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BatchMongoEventListenerTest {

  private static final String BATCH_CODE = "batch_code";

  @Autowired
  private BatchRepository batchRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private DiscussionRepository discussionRepository;

  @Autowired
  private FileRepositoryV2 fileRepository;

  @Autowired
  private SharedCourseRepository sharedCourseRepository;

  private Batch batch;

  private SharedCourse sharedCourse;

  private Discussion discussion;

  private FileV2 file;

  private User student;

  @Before
  public void setUp() {

    file = FileV2.builder()
      .used(true)
      .build();
    fileRepository.save(file);

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();
    batchRepository.save(batch);

    student = User.builder()
      .batch(batch)
      .role(Role.STUDENT)
      .build();
    userRepository.save(student);

    Course course = Course.builder()
      .file(file)
      .build();
    sharedCourse = SharedCourse.builder()
      .batch(batch)
      .course(course)
      .build();
    sharedCourseRepository.save(sharedCourse);

    discussion = Discussion.builder()
      .courseId(sharedCourse.getId())
      .batchId(batch.getId())
      .batchCode(BATCH_CODE)
      .build();
    discussionRepository.save(discussion);
  }

  @After
  public void tearDown() {

    batchRepository.deleteAll();
    userRepository.deleteAll();
    discussionRepository.deleteAll();
    fileRepository.deleteAll();
    sharedCourseRepository.deleteAll();
  }

  @Test
  public void testGivenNonMarkedDeletedBatchByListeningToBeforeConversionEventToDeleteBatchRelatedDataReturnNoDeletion() {

    batch.setDeleted(false);

    batchRepository.save(batch);

    FileV2 foundFileAfterBatchDeletion = fileRepository.findOne(file.getId());
    assertThat(foundFileAfterBatchDeletion).isNotNull();
    assertThat(foundFileAfterBatchDeletion.isUsed()).isTrue();

    User foundStudentAfterBatchDeletion = userRepository.findOne(
      student.getId());
    assertThat(foundStudentAfterBatchDeletion).isNotNull();
    assertThat(foundStudentAfterBatchDeletion.isDeleted()).isFalse();

    assertThat(
      sharedCourseRepository.findOne(sharedCourse.getId())).isNotNull();

    assertThat(discussionRepository.findOne(discussion.getId())).isNotNull();
  }

  @Test
  public void testGivenMarkedDeletedBatchByListeningToBeforeConversionEventToDeleteBatchRelatedDataReturnBatchRelatedDataDeletion() {

    batch.setDeleted(true);

    batchRepository.save(batch);

    FileV2 foundFileAfterBatchDeletion = fileRepository.findOne(file.getId());
    assertThat(foundFileAfterBatchDeletion).isNotNull();
    assertThat(foundFileAfterBatchDeletion.isUsed()).isFalse();

    User foundStudentAfterBatchDeletion = userRepository.findOne(
      student.getId());
    assertThat(foundStudentAfterBatchDeletion).isNotNull();
    assertThat(foundStudentAfterBatchDeletion.isDeleted()).isTrue();

    assertThat(sharedCourseRepository.findOne(sharedCourse.getId())).isNull();

    assertThat(discussionRepository.findOne(discussion.getId())).isNull();
  }

}
