package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.DiscussionRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.SharedCourseService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DiscussionServiceImplTest {
  
  private static final String BATCH_ID = "batch-id";
  
  private static final String BATCH_CODE = "batch-code";
  
  private static final Batch BATCH = Batch.builder()
    .id(BATCH_ID)
    .code(BATCH_CODE)
    .build();
  
  private static final String COURSE_ID = "course-id";

  private static final String ID = "id";

  private static final String COMMENT = "comment";

  private static final String EMAIL = "email";

  private static final String USER_ID = "user-id";

  private static final String NAME = "name";

  private static final int PAGE = 1;

  private static final int SIZE = 4;

  private static final Pageable PAGEABLE = new PageRequest(PAGE - 1, SIZE);

  private static final User VALID_USER_MENTOR = User.builder()
    .id(USER_ID)
    .role(Role.MENTOR)
    .name(NAME)
    .build();

  private static final Discussion DISCUSSION = Discussion.builder()
    .id(ID)
    .courseId(COURSE_ID)
    .batchId(BATCH_ID)
    .batchCode(BATCH_CODE)
    .description(COMMENT)
    .user(VALID_USER_MENTOR)
    .build();

  private static final User VALID_USER_STUDENT = User.builder()
    .role(Role.STUDENT)
    .batch(Batch.builder()
             .code(BATCH_CODE)
             .build())
    .build();

  private static final User INVALID_USER_STUDENT = User.builder()
    .role(Role.STUDENT)
    .batch(Batch.builder()
             .code("a")
             .build())
    .build();

  private Discussion discussionFromRequest;

  @Mock
  private DiscussionRepository discussionRepository;

  @Mock
  private UserService userService;

  @Mock
  private SharedCourseService sharedCourseService;

  @Mock
  private BatchService batchService;

  @InjectMocks
  private DiscussionServiceImpl discussionService;

  @Before
  public void setUp() {

    User user = User.builder()
      .email(EMAIL)
      .build();
    discussionFromRequest = Discussion.builder()
      .id(ID)
      .courseId(COURSE_ID)
      .batchCode(BATCH_CODE)
      .description(COMMENT)
      .user(user)
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      discussionRepository, userService, sharedCourseService, batchService);
  }

  @Test
  public void testGivenCourseIdAndBatchCodeAndPageableByGettingDiscussionsReturnPageOfDiscussion() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(VALID_USER_MENTOR);
    when(sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                       BATCH_CODE
    )).thenReturn(new Course());

    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    
    Page<Discussion> discussionPage = PageHelper.toPage(
      Collections.singletonList(DISCUSSION), PAGEABLE);
    when(discussionRepository.findAllByCourseIdAndBatchIdOrderByCreatedAtDesc(
      COURSE_ID, BATCH_ID, PAGEABLE)).thenReturn(discussionPage);
    
    Page<Discussion> discussions = discussionService.getDiscussions(
      EMAIL, COURSE_ID, BATCH_CODE, PAGEABLE);

    assertThat(discussions).isNotNull();
    assertThat(discussions).isEqualTo(discussionPage);

    verify(userService).getUserByEmail(EMAIL);
    verify(sharedCourseService).getCourseByIdAndBatchCode(
      COURSE_ID, BATCH_CODE);
    verify(
      discussionRepository).findAllByCourseIdAndBatchIdOrderByCreatedAtDesc(
      COURSE_ID, BATCH_ID, PAGEABLE);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testGivenInvalidCourseIdAndBatchCodeAndPageableByGettingDiscussionsReturnEmptyPageOfDiscussion() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(VALID_USER_STUDENT);
    when(sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                       BATCH_CODE
    )).thenReturn(null);

    Page<Discussion> discussionPage = PageHelper.empty(PAGEABLE);

    Page<Discussion> discussions = discussionService.getDiscussions(
      EMAIL, COURSE_ID, BATCH_CODE, PAGEABLE);

    assertThat(discussions).isNotNull();
    assertThat(discussions).isEqualTo(discussionPage);

    verify(userService).getUserByEmail(EMAIL);
    verify(sharedCourseService).getCourseByIdAndBatchCode(
      COURSE_ID, BATCH_CODE);
    verifyZeroInteractions(discussionRepository, batchService);
  }

  @Test
  public void testGivenInvalidUserByGettingDiscussionsReturnEmptyPageOfDiscussion() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(INVALID_USER_STUDENT);

    catchException(
      () -> discussionService.getDiscussions(EMAIL, COURSE_ID, BATCH_CODE,
                                             PAGEABLE
      ));

    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid Batch");

    verify(userService).getUserByEmail(EMAIL);
    verifyZeroInteractions(
      discussionRepository, sharedCourseService, batchService);
  }

  @Test
  public void testGivenDiscussionByCreatingDiscussionReturnCreatedDiscussionObject() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(VALID_USER_MENTOR);
    when(sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                       BATCH_CODE
    )).thenReturn(new Course());
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(BATCH);
    when(discussionRepository.save(DISCUSSION)).thenReturn(DISCUSSION);

    Discussion discussion = discussionService.createDiscussion(
      discussionFromRequest);

    assertThat(discussion).isNotNull();
    assertThat(discussion).isEqualTo(DISCUSSION);

    verify(userService, times(2)).getUserByEmail(EMAIL);
    verify(sharedCourseService).getCourseByIdAndBatchCode(COURSE_ID,
                                                          BATCH_CODE
    );
    verify(discussionRepository).save(DISCUSSION);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testGivenDiscussionWithInvalidCourseIdAndBatchCodeByCreatingDiscussionReturnUnsupportedOperationException() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(VALID_USER_STUDENT);

    when(sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                       BATCH_CODE
    )).thenReturn(null);

    catchException(
      () -> discussionService.createDiscussion(discussionFromRequest));

    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Create Discussion Failed");

    verify(userService).getUserByEmail(EMAIL);
    verify(sharedCourseService).getCourseByIdAndBatchCode(COURSE_ID,
                                                          BATCH_CODE
    );
    verifyZeroInteractions(discussionRepository, batchService);
  }

  @Test
  public void testGivenDiscussionWithInvalidUserByCreatingDiscussionReturnForbiddenException() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(INVALID_USER_STUDENT);

    catchException(
      () -> discussionService.createDiscussion(discussionFromRequest));

    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid Batch");

    verify(userService).getUserByEmail(EMAIL);
    verifyZeroInteractions(discussionRepository, sharedCourseService);
  }

}
