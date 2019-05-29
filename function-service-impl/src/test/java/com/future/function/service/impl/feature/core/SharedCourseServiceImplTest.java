package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.SharedCourse;
import com.future.function.repository.feature.core.SharedCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.service.api.feature.core.ResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
  
  private static final Course COURSE = Course.builder()
    .id(COURSE_ID)
    .title(COURSE_TITLE)
    .description(COURSE_DESCRIPTION)
    .file(FILE)
    .build();
  
  private static final List<Course> COURSE_LIST = Collections.singletonList(
    COURSE);
  
  private static final Batch BATCH = Batch.builder()
    .code(BATCH_CODE)
    .build();
  
  private static final SharedCourse SHARED_COURSE = SharedCourse.builder()
    .course(COURSE)
    .batch(BATCH)
    .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 5);
  
  private static final List<SharedCourse> SHARED_COURSE_LIST =
    Collections.singletonList(SHARED_COURSE);
  
  private static final Page<SharedCourse> SHARED_COURSE_PAGE = new PageImpl<>(
    SHARED_COURSE_LIST, PAGEABLE, SHARED_COURSE_LIST.size());
  
  @Mock
  private SharedCourseRepository sharedCourseRepository;
  
  @Mock
  private BatchService batchService;
  
  @Mock
  private ResourceService resourceService;
  
  @Mock
  private CourseService courseService;
  
  @InjectMocks
  private SharedCourseServiceImpl sharedCourseService;
  
  @Before
  public void setUp() {
  
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(
      sharedCourseRepository, batchService, resourceService, courseService);
  }
  
  @Test
  public void testGivenCourseIdAndBatchCodeByGettingCourseForBatchReturnCourseObject() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH
    )).thenReturn(Optional.of(SHARED_COURSE));
    
    Course foundCourse = sharedCourseService.getCourseByIdAndBatchCode(
      COURSE_ID, BATCH_CODE);
    
    assertThat(foundCourse).isNotNull();
    assertThat(foundCourse).isEqualTo(COURSE);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService);
  }
  
  @Test
  public void testGivenInvalidCourseIdAndBatchCodeByGettingCourseForBatchReturnNotFoundException() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH
    )).thenReturn(Optional.empty());
    
    catchException(
      () -> sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                          BATCH_CODE
      ));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Course Not Found");
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService);
  }
  
  @Test
  public void testGivenBatchCodeByGettingCoursesForBatchReturnPageOfCourse() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findAllByBatch(BATCH, PAGEABLE)).thenReturn(
      SHARED_COURSE_PAGE);
    
    Page<Course> expectedCoursePage = new PageImpl<>(
      Collections.singletonList(COURSE), PAGEABLE, 1);
    
    Page<Course> coursePage = sharedCourseService.getCoursesByBatchCode(
      BATCH_CODE, PAGEABLE);
    
    assertThat(coursePage).isNotNull();
    assertThat(coursePage).isEqualTo(expectedCoursePage);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findAllByBatch(BATCH, PAGEABLE);
    verifyZeroInteractions(resourceService, courseService);
  }
  
  @Test
  public void testGivenBatchCodeWithNoSharedCoursesByGettingCoursesForBatchReturnEmptyPageOfCourse() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    PageImpl<SharedCourse> sharedCoursePage = new PageImpl<>(
      Collections.emptyList(), PAGEABLE, 0);
    when(sharedCourseRepository.findAllByBatch(BATCH, PAGEABLE)).thenReturn(
      sharedCoursePage);
    
    Page<Course> expectedCoursePage = new PageImpl<>(
      Collections.emptyList(), PAGEABLE, 0);
    
    Page<Course> coursePage = sharedCourseService.getCoursesByBatchCode(
      BATCH_CODE, PAGEABLE);
    
    assertThat(coursePage).isNotNull();
    assertThat(coursePage).isEqualTo(expectedCoursePage);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findAllByBatch(BATCH, PAGEABLE);
    verifyZeroInteractions(resourceService, courseService);
  }
  
  @Test
  public void testGivenCourseIdAndBatchCodeByDeletingCourseForBatchReturnSuccessfulDeletion() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH
    )).thenReturn(Optional.of(SHARED_COURSE));
    
    sharedCourseService.deleteCourseByIdAndBatchCode(COURSE_ID, BATCH_CODE);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(sharedCourseRepository).delete(SHARED_COURSE);
    verifyZeroInteractions(courseService);
  }
  
  @Test
  public void testGivenInvalidCourseIdAndBatchCodeByDeletingCourseForBatchReturnFailedDeletion() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH
    )).thenReturn(Optional.empty());
    
    sharedCourseService.deleteCourseByIdAndBatchCode(COURSE_ID, BATCH_CODE);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService);
  }
  
  @Test
  public void testGivenCourseIdAndBatchCodeAndCourseByUpdatingCourseForBatchReturnUpdatedCourse() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH
    )).thenReturn(Optional.of(SHARED_COURSE));
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE);
    when(sharedCourseRepository.save(SHARED_COURSE)).thenReturn(SHARED_COURSE);
    
    Course updatedCourse = sharedCourseService.updateCourseForBatch(
      COURSE_ID, BATCH_CODE, COURSE);
    
    assertThat(updatedCourse).isNotNull();
    assertThat(updatedCourse).isEqualTo(COURSE);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(FILE_ID);
    verify(sharedCourseRepository).save(SHARED_COURSE);
    verifyZeroInteractions(courseService);
  }
  
  @Test
  public void testInvalidGivenCourseIdAndBatchCodeAndCourseByUpdatingCourseForBatchReturnCourseFromParameter() {
    
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.findByCourseIdAndBatch(COURSE_ID,
                                                       BATCH
    )).thenReturn(Optional.empty());
    
    Course updatedCourse = sharedCourseService.updateCourseForBatch(
      COURSE_ID, BATCH_CODE, COURSE);
    
    assertThat(updatedCourse).isNotNull();
    assertThat(updatedCourse).isEqualTo(COURSE);
    
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).findByCourseIdAndBatch(COURSE_ID, BATCH);
    verifyZeroInteractions(resourceService, courseService);
  }
  
  @Test
  public void testGivenCourseIdsAndOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromAnotherBatchReturnListOfCourse() {
    
    String originBatchCode = "origin-batch-code";
    Batch originBatch = Batch.builder()
      .code(originBatchCode)
      .build();
    when(batchService.getBatchByCode(originBatchCode)).thenReturn(originBatch);
    when(sharedCourseRepository.findAllByBatch(originBatch)).thenReturn(
      Stream.of(SHARED_COURSE));
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.save(SHARED_COURSE)).thenReturn(SHARED_COURSE);
  
    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      COURSE_IDS, originBatchCode, BATCH_CODE);
  
    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(COURSE_LIST);
    
    verify(batchService).getBatchByCode(originBatchCode);
    verify(sharedCourseRepository).findAllByBatch(originBatch);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).save(SHARED_COURSE);
  }
  
  @Test
  public void testGivenCourseIdsAndNullOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromMasterDataReturnListOfCourse() {
    
    when(courseService.getCourse(COURSE_ID)).thenReturn(COURSE);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.save(SHARED_COURSE)).thenReturn(SHARED_COURSE);
    
    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      COURSE_IDS, null, BATCH_CODE);
    
    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(COURSE_LIST);
    
    verify(courseService).getCourse(COURSE_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).save(SHARED_COURSE);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenCourseIdsAndEmptyOriginBatchCodeAndTargetBatchCodeByCreatingCourseForBatchFromMasterDataReturnListOfCourse() {
    
    when(courseService.getCourse(COURSE_ID)).thenReturn(COURSE);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(sharedCourseRepository.save(SHARED_COURSE)).thenReturn(SHARED_COURSE);
    
    List<Course> createdCourseList = sharedCourseService.createCourseForBatch(
      COURSE_IDS, "", BATCH_CODE);
    
    assertThat(createdCourseList).isNotEmpty();
    assertThat(createdCourseList).isEqualTo(COURSE_LIST);
    
    verify(courseService).getCourse(COURSE_ID);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(sharedCourseRepository).save(SHARED_COURSE);
    verifyZeroInteractions(resourceService);
  }
  
}
