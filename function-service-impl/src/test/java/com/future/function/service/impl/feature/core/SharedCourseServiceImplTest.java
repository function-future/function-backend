package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
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
import com.future.function.service.impl.helper.PageHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SharedCourseServiceImplTest {

  private static final String COURSE_ID = "course-id";

  private static final List<String> COURSE_IDS = Collections.singletonList(
    COURSE_ID);

  private static final String BATCH_CODE = "batch-code";

  private static final String COURSE_TITLE = "course-title";

  private static final String COURSE_DESCRIPTION = "course-description";

  private static final String FILE_ID = "file-id";

  private static final List<String> FILE_IDS = Collections.singletonList(
    FILE_ID);

  private static final FileV2 FILE = FileV2.builder()
    .id(FILE_ID)
    .build();

  private static final String SHARED_COURSE_ID = "shared-course-id";

  private static final Batch BATCH = Batch.builder()
    .code(BATCH_CODE)
    .build();

  private static final String EMAIL = "email";

  private static final Pageable PAGEABLE = new PageRequest(0, 5);

  private Course course;

  private List<Course> courseList;

  private SharedCourse sharedCourse;

  private Discussion discussion;

  @Mock
  private SharedCourseRepository sharedCourseRepository;

  @Mock
  private BatchService batchService;

  @Mock
  private ResourceService resourceService;

  @Mock
  private CourseService courseService;

  @Mock
  private DiscussionService discussionService;

  @InjectMocks
  private SharedCourseServiceImpl sharedCourseService;

  @Before
  public void setUp() {

    course = Course.builder()
      .id(COURSE_ID)
      .title(COURSE_TITLE)
      .description(COURSE_DESCRIPTION)
      .file(FILE)
      .build();

    courseList = Collections.singletonList(course);

    sharedCourse = SharedCourse.builder()
      .course(course)
      .batch(BATCH)
      .build();

    discussion = Discussion.builder()
      .courseId(COURSE_ID)
      .batchCode(BATCH_CODE)
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(sharedCourseRepository, batchService,
                             resourceService, courseService, discussionService
    );
  }

  @Test
  public void testGivenCourseIdAndBatchCodeByGettingCourseForBatchReturnCourseObject() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    sharedCourse.setId(SHARED_COURSE_ID);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.of(sharedCourse));

    Course foundCourse = sharedCourseService.getCourseByIdAndBatchCode(
      COURSE_ID, BATCH_CODE);

    assertThat(foundCourse).isNotNull();
    assertThat(foundCourse).isEqualTo(Course.builder()
                                        .id(SHARED_COURSE_ID)
                                        .title(COURSE_TITLE)
                                        .description(COURSE_DESCRIPTION)
                                        .file(FILE)
                                        .build());

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService, discussionService);
  }

  @Test
  public void testGivenInvalidCourseIdAndBatchCodeByGettingCourseForBatchReturnNotFoundException() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.empty());

    catchException(
      () -> sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                          BATCH_CODE
      ));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Course Not Found");

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService, discussionService);
  }

  @Test
  public void testGivenBatchCodeByGettingCoursesForBatchReturnPageOfCourse() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);

    sharedCourse.setId(SHARED_COURSE_ID);
    List<SharedCourse> sharedCourseList = Collections.singletonList(
      sharedCourse);
    Page<SharedCourse> sharedCoursePage = PageHelper.toPage(
      sharedCourseList, PAGEABLE);
    when(sharedCourseRepository.findAllByBatch(BATCH, PAGEABLE)).thenReturn(
      sharedCoursePage);

    Page<Course> expectedCoursePage = PageHelper.toPage(
      Collections.singletonList(Course.builder()
                                  .id(SHARED_COURSE_ID)
                                  .title(COURSE_TITLE)
                                  .description(COURSE_DESCRIPTION)
                                  .file(FILE)
                                  .build()), PAGEABLE);

    Page<Course> coursePage = sharedCourseService.getCoursesByBatchCode(
      BATCH_CODE, PAGEABLE);

    assertThat(coursePage).isNotNull();
    assertThat(coursePage).isEqualTo(expectedCoursePage);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findAllByBatch(BATCH, PAGEABLE);
    verifyZeroInteractions(resourceService, courseService, discussionService);
  }

  @Test
  public void testGivenBatchCodeWithNoSharedCoursesByGettingCoursesForBatchReturnEmptyPageOfCourse() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    Page<SharedCourse> sharedCoursePage = PageHelper.empty(PAGEABLE);
    when(sharedCourseRepository.findAllByBatch(BATCH, PAGEABLE)).thenReturn(
      sharedCoursePage);

    Page<Course> expectedCoursePage = PageHelper.empty(PAGEABLE);

    Page<Course> coursePage = sharedCourseService.getCoursesByBatchCode(
      BATCH_CODE, PAGEABLE);

    assertThat(coursePage).isNotNull();
    assertThat(coursePage).isEqualTo(expectedCoursePage);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findAllByBatch(BATCH, PAGEABLE);
    verifyZeroInteractions(resourceService, courseService, discussionService);
  }

  @Test
  public void testGivenCourseIdAndBatchCodeByDeletingCourseForBatchReturnSuccessfulDeletion() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.of(sharedCourse));

    sharedCourseService.deleteCourseByIdAndBatchCode(COURSE_ID, BATCH_CODE);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(discussionService).deleteDiscussions(
      sharedCourse.getId(), BATCH_CODE);
    verify(sharedCourseRepository).delete(sharedCourse);
    verifyZeroInteractions(courseService);
  }

  @Test
  public void testGivenInvalidCourseIdAndBatchCodeByDeletingCourseForBatchReturnFailedDeletion() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.empty());

    sharedCourseService.deleteCourseByIdAndBatchCode(COURSE_ID, BATCH_CODE);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService);
  }

  @Test
  public void testGivenCourseIdAndBatchCodeAndCourseByUpdatingCourseForBatchReturnUpdatedCourse() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.of(sharedCourse));
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE);
    when(sharedCourseRepository.save(sharedCourse)).thenReturn(sharedCourse);

    Course updatedCourse = sharedCourseService.updateCourseForBatch(
      COURSE_ID, BATCH_CODE, course);

    assertThat(updatedCourse).isNotNull();
    assertThat(updatedCourse).isEqualTo(course);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(FILE_ID);
    verify(sharedCourseRepository).save(sharedCourse);
    verifyZeroInteractions(courseService, discussionService);
  }

  @Test
  public void testInvalidGivenCourseIdAndBatchCodeAndCourseByUpdatingCourseForBatchReturnCourseFromParameter() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.empty());

    Course updatedCourse = sharedCourseService.updateCourseForBatch(
      COURSE_ID, BATCH_CODE, course);

    assertThat(updatedCourse).isNotNull();
    assertThat(updatedCourse).isEqualTo(course);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService, discussionService);
  }

  @Test
  public void testGivenCourseIdsAndOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromAnotherBatchReturnListOfCourse() {

    String originBatchCode = "origin-batch-code";
    Batch originBatch = Batch.builder()
      .code(originBatchCode)
      .build();
    when(batchService.getBatchByCode(originBatchCode)).thenReturn(originBatch);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findAllByBatch(originBatch)).thenReturn(
      Stream.of(sharedCourse));

    FileV2 copiedFile = new FileV2();
    BeanUtils.copyProperties(FILE, copiedFile);
    String copiedFileId = "copied-file-id";
    copiedFile.setId(copiedFileId);
    when(resourceService.createACopy(FILE, FileOrigin.COURSE)).thenReturn(
      copiedFile);
    when(sharedCourseRepository.save(sharedCourse)).thenReturn(sharedCourse);

    List<String> sharedCourseIds = Collections.singletonList(
      sharedCourse.getId());
    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      sharedCourseIds, originBatchCode, BATCH_CODE);

    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(courseList);
    assertThat(createdCourseList.get(0)
                 .getFile()).isEqualTo(copiedFile);

    verify(batchService).getBatchByCode(originBatchCode);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findAllByBatch(originBatch);
    verify(resourceService).createACopy(FILE, FileOrigin.COURSE);
    verify(sharedCourseRepository).save(sharedCourse);
    verifyZeroInteractions(discussionService);
  }

  @Test
  public void testGivenCourseIdsAndNoMaterialCourseAndOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromAnotherBatchReturnListOfCourse() {

    String originBatchCode = "origin-batch-code";
    Batch originBatch = Batch.builder()
      .code(originBatchCode)
      .build();
    when(batchService.getBatchByCode(originBatchCode)).thenReturn(originBatch);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    sharedCourse.getCourse().setFile(new FileV2());
    when(sharedCourseRepository.findAllByBatch(originBatch)).thenReturn(
      Stream.of(sharedCourse));

    when(sharedCourseRepository.save(sharedCourse)).thenReturn(sharedCourse);

    List<String> sharedCourseIds = Collections.singletonList(
      sharedCourse.getId());
    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      sharedCourseIds, originBatchCode, BATCH_CODE);

    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(courseList);
    assertThat(createdCourseList.get(0)
                 .getFile()).isEqualTo(new FileV2());

    verify(batchService).getBatchByCode(originBatchCode);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findAllByBatch(originBatch);
    verify(sharedCourseRepository).save(sharedCourse);
    verifyZeroInteractions(resourceService, discussionService);
  }

  @Test
  public void testGivenCourseIdsAndNullOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromMasterDataReturnListOfCourse() {

    when(courseService.getCourse(COURSE_ID)).thenReturn(course);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);

    FileV2 copiedFile = new FileV2();
    BeanUtils.copyProperties(FILE, copiedFile);
    String copiedFileId = "copied-file-id";
    copiedFile.setId(copiedFileId);
    when(resourceService.createACopy(FILE, FileOrigin.COURSE)).thenReturn(
      copiedFile);
    when(sharedCourseRepository.save(sharedCourse)).thenReturn(sharedCourse);

    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      COURSE_IDS, null, BATCH_CODE);

    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(courseList);
    assertThat(createdCourseList.get(0)
                 .getFile()).isEqualTo(copiedFile);

    verify(courseService).getCourse(COURSE_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(resourceService).createACopy(FILE, FileOrigin.COURSE);
    verify(sharedCourseRepository).save(sharedCourse);
    verifyZeroInteractions(discussionService);
  }

  @Test
  public void testGivenCourseIdsAndEmptyOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromMasterDataReturnListOfCourse() {

    when(courseService.getCourse(COURSE_ID)).thenReturn(course);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);

    FileV2 copiedFile = new FileV2();
    BeanUtils.copyProperties(FILE, copiedFile);
    String copiedFileId = "copied-file-id";
    copiedFile.setId(copiedFileId);
    when(resourceService.createACopy(FILE, FileOrigin.COURSE)).thenReturn(
      copiedFile);
    when(sharedCourseRepository.save(sharedCourse)).thenReturn(sharedCourse);

    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      COURSE_IDS, "", BATCH_CODE);

    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(courseList);
    assertThat(createdCourseList.get(0)
                 .getFile()).isEqualTo(copiedFile);

    verify(courseService).getCourse(COURSE_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(resourceService).createACopy(FILE, FileOrigin.COURSE);
    verify(sharedCourseRepository).save(sharedCourse);
    verifyZeroInteractions(discussionService);
  }

  @Test
  public void testGivenCourseIdsAndNoMaterialCourseAndEmptyOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromMasterDataReturnListOfCourse() {

    course.setFile(new FileV2());
    when(courseService.getCourse(COURSE_ID)).thenReturn(course);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);

    when(sharedCourseRepository.save(sharedCourse)).thenReturn(sharedCourse);

    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      COURSE_IDS, "", BATCH_CODE);

    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(courseList);
    assertThat(createdCourseList.get(0)
                 .getFile()).isEqualTo(new FileV2());

    verify(courseService).getCourse(COURSE_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).save(sharedCourse);
    verifyZeroInteractions(resourceService, discussionService);
  }

  @Test
  public void testGivenEmailAndCourseIdAndBatchCodeByGettingDiscussionsForSharedCourseReturnPageOfDiscussion() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.of(sharedCourse));
    when(discussionService.getDiscussions(EMAIL, COURSE_ID, BATCH_CODE,
                                          PAGEABLE
    )).thenReturn(
      PageHelper.toPage(Collections.singletonList(discussion), PAGEABLE));

    Page<Discussion> expectedDiscussions = new PageImpl<>(
      Collections.singletonList(discussion), PAGEABLE, 1);

    Page<Discussion> discussions = sharedCourseService.getDiscussions(EMAIL,
                                                                      COURSE_ID,
                                                                      BATCH_CODE,
                                                                      PAGEABLE
    );

    assertThat(discussions).isNotNull();
    assertThat(discussions).isEqualTo(expectedDiscussions);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verify(discussionService).getDiscussions(EMAIL, COURSE_ID, BATCH_CODE,
                                             PAGEABLE
    );

    verifyZeroInteractions(courseService, resourceService);
  }

  @Test
  public void testGivenDiscussionByCreatingDiscussionReturnCreatedDiscussion() {

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByIdAndBatch(COURSE_ID, BATCH)).thenReturn(
      Optional.of(sharedCourse));

    Discussion discussionRequest = Discussion.builder()
      .courseId(COURSE_ID)
      .batchCode(BATCH_CODE)
      .build();
    when(discussionService.createDiscussion(discussionRequest)).thenReturn(
      discussion);

    Discussion discussion = sharedCourseService.createDiscussion(
      discussionRequest);

    assertThat(discussion).isNotNull();
    assertThat(discussion).isEqualTo(this.discussion);

    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByIdAndBatch(COURSE_ID, BATCH);
    verify(discussionService).createDiscussion(discussionRequest);

    verifyZeroInteractions(courseService, resourceService);
  }

}
