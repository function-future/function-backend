package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.ActivityBlogRepository;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
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
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityBlogServiceImplTest {

  private static final String EMAIL = "email";

  private static final String USER_ID = "user-id";

  private static final PageRequest PAGEABLE = new PageRequest(0, 5);

  private static final String ID = "id";

  private static final String FILE_ID = "file-id";

  private static final List<String> FILE_IDS = Collections.singletonList(
    FILE_ID);

  private static final FileV2 FILE_V2 = FileV2.builder()
    .id(FILE_ID)
    .build();

  private static final List<FileV2> FILES = Collections.singletonList(FILE_V2);

  private static final User USER = User.builder()
    .id(USER_ID)
    .email(EMAIL)
    .build();

  private ActivityBlog activityBlog = ActivityBlog.builder()
    .id(ID)
    .files(FILES)
    .user(USER)
    .build();

  @Mock
  private ActivityBlogRepository activityBlogRepository;

  @Mock
  private ResourceService resourceService;

  @Mock
  private UserService userService;

  @InjectMocks
  private ActivityBlogServiceImpl activityBlogService;

  @Before
  public void setUp() {

    activityBlog.setCreatedBy(USER_ID);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      activityBlogRepository, resourceService, userService);
  }

  @Test
  public void testGivenActivityBlogIdByFindingActivityBlogReturnActivityBlog() {

    when(activityBlogRepository.findOne(ID)).thenReturn(activityBlog);

    ActivityBlog retrievedActivityBlog = activityBlogService.getActivityBlog(
      ID);

    assertThat(retrievedActivityBlog).isNotNull();
    assertThat(retrievedActivityBlog).isEqualTo(activityBlog);

    verify(activityBlogRepository).findOne(ID);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenNotExistingActivityBlogIdByFindingActivityBlogReturnNotFoundException() {

    when(activityBlogRepository.findOne(ID)).thenReturn(null);

    catchException(() -> activityBlogService.getActivityBlog(ID));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Activity Blog Not Found");

    verify(activityBlogRepository).findOne(ID);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenPageableByFindingActivityBlogsReturnPageOfActivityBlog() {

    String userId = "551137c2f9e1fac808a5f572";
    Page<ActivityBlog> activityBlogPage = PageHelper.toPage(
      Collections.singletonList(activityBlog), PAGEABLE);
    when(activityBlogRepository.findAll(userId, "", PAGEABLE)).thenReturn(
      activityBlogPage);

    Page<ActivityBlog> activityBlogs = activityBlogService.getActivityBlogs(
      userId, "", PAGEABLE);

    assertThat(activityBlogs).isNotNull();
    assertThat(activityBlogs).isEqualTo(activityBlogPage);

    verify(activityBlogRepository).findAll(userId, "", PAGEABLE);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenPageableAndEmptyDatabaseByFindingActivityBlogsReturnEmptyPage() {

    String userId = "551137c2f9e1fac808a5f572";
    Page<ActivityBlog> activityBlogPage = PageHelper.empty(PAGEABLE);
    when(activityBlogRepository.findAll(userId, "", PAGEABLE)).thenReturn(
      activityBlogPage);

    Page<ActivityBlog> activityBlogs = activityBlogService.getActivityBlogs(
      userId, "", PAGEABLE);

    assertThat(activityBlogs).isNotNull();
    assertThat(activityBlogs).isEqualTo(activityBlogPage);

    verify(activityBlogRepository).findAll(userId, "", PAGEABLE);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenPageableAndNonObjectIdCompliantUserIdByFindingActivityBlogsReturnEmptyPage() {

    String nonObjectIdCompliantUserId = "random-id";
    Page<ActivityBlog> activityBlogPage = PageHelper.empty(PAGEABLE);

    Page<ActivityBlog> activityBlogs = activityBlogService.getActivityBlogs(
      nonObjectIdCompliantUserId, "", PAGEABLE);

    assertThat(activityBlogs).isNotNull();
    assertThat(activityBlogs).isEqualTo(activityBlogPage);

    verifyZeroInteractions(
      activityBlogRepository, resourceService, userService);
  }

  @Test
  public void testGivenEmailAndActivityBlogIdByDeletingActivityBlogReturnSuccessfulDeletion() {

    when(activityBlogRepository.findOne(ID)).thenReturn(activityBlog);
    when(resourceService.markFilesUsed(Collections.singletonList(FILE_ID),
                                       false
    )).thenReturn(true);

    activityBlogService.deleteActivityBlog(USER_ID, Role.ADMIN, ID);

    verify(activityBlogRepository).findOne(ID);
    verify(resourceService).markFilesUsed(Collections.singletonList(FILE_ID),
                                          false
    );
    verify(activityBlogRepository).delete(activityBlog);
    verifyZeroInteractions(userService);
  }

  @Test
  public void testGivenEmailAndNotExistingActivityBlogIdByDeletingActivityBlogsReturnFailedDeletion() {

    when(activityBlogRepository.findOne(ID)).thenReturn(null);

    activityBlogService.deleteActivityBlog(USER_ID, Role.ADMIN, ID);

    verify(activityBlogRepository).findOne(ID);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenEmailAndInvalidUserByDeletingActivityBlogsReturnForbiddenException() {

    when(activityBlogRepository.findOne(ID)).thenReturn(activityBlog);

    catchException(
      () -> activityBlogService.deleteActivityBlog(USER_ID + "2", Role.STUDENT,
                                                   ID
      ));

    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid User Role");

    verify(activityBlogRepository).findOne(ID);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenActivityBlogDataByCreatingActivityBlogReturnCreatedActivityBlog() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(USER);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(activityBlogRepository.save(activityBlog)).thenReturn(activityBlog);

    ActivityBlog createdActivityBlog = activityBlogService.createActivityBlog(
      activityBlog);

    assertThat(createdActivityBlog).isNotNull();
    assertThat(createdActivityBlog).isEqualTo(activityBlog);

    verify(userService).getUserByEmail(EMAIL);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(activityBlogRepository).save(activityBlog);
  }

  @Test
  public void testGivenActivityBlogDataButFailedToCreateByCreatingActivityBlogReturnUnsupportedOperationException() {

    when(userService.getUserByEmail(EMAIL)).thenReturn(USER);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(activityBlogRepository.save(activityBlog)).thenReturn(null);

    catchException(() -> activityBlogService.createActivityBlog(activityBlog));

    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Create Activity Blog Failed");

    verify(userService).getUserByEmail(EMAIL);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(activityBlogRepository).save(activityBlog);
  }

  @Test
  public void testGivenActivityBlogDataByUpdatingActivityBlogReturnUpdatedActivityBlog() {

    when(activityBlogRepository.findOne(ID)).thenReturn(activityBlog);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(activityBlogRepository.save(activityBlog)).thenReturn(activityBlog);

    ActivityBlog updatedActivityBlog = activityBlogService.updateActivityBlog(
      USER_ID, Role.ADMIN, activityBlog);

    assertThat(updatedActivityBlog).isNotNull();
    assertThat(updatedActivityBlog).isEqualTo(activityBlog);

    verify(activityBlogRepository).findOne(ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(activityBlogRepository).save(activityBlog);
    verifyZeroInteractions(userService);
  }

  @Test
  public void testGivenActivityBlogDataAndNotExistingIdByUpdatingActivityBlogReturnRequestActivityBlogObject() {

    when(activityBlogRepository.findOne(ID)).thenReturn(null);

    ActivityBlog updatedActivityBlog = activityBlogService.updateActivityBlog(
      USER_ID, Role.ADMIN, activityBlog);

    assertThat(updatedActivityBlog).isNotNull();
    assertThat(updatedActivityBlog).isEqualTo(activityBlog);

    verify(activityBlogRepository).findOne(ID);
    verifyZeroInteractions(resourceService, userService);
  }

  @Test
  public void testGivenActivityBlogDataAndInvalidUserByUpdatingActivityBlogReturnForbiddenException() {

    when(activityBlogRepository.findOne(ID)).thenReturn(activityBlog);

    ActivityBlog activityBlog = new ActivityBlog();
    BeanUtils.copyProperties(this.activityBlog, activityBlog);

    User user = User.builder()
      .email(USER_ID + "2")
      .build();
    activityBlog.setUser(user);

    catchException(
      () -> activityBlogService.updateActivityBlog(user.getId(), Role.STUDENT,
                                                   activityBlog
      ));

    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid User Role");

    verify(activityBlogRepository).findOne(ID);
    verifyZeroInteractions(resourceService, userService);
  }

}
