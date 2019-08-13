package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.scoring.ScoringMediatorService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  private static final String ADDRESS = "address";

  private static final String EMAIL_MENTOR = "mentor@test.com";

  private static final String EMAIL_STUDENT = "student@test.com";

  private static final String NAME_MENTOR = "name-mentor";

  private static final String NAME_STUDENT = "name-student";

  private static final String PASSWORD = "password";

  private static final String NON_EXISTING_USER_ID = "non-existing-user-id";

  private static final String NUMBER = "1";

  private static final Batch BATCH = Batch.builder()
    .code(NUMBER)
    .build();

  private static final String PHONE = "phone";

  private static final String PICTURE_ID = "picture-id";

  private static final List<String> FILE_IDS = Collections.singletonList(
    PICTURE_ID);

  private static final FileV2 PICTURE = FileV2.builder()
    .id(PICTURE_ID)
    .asResource(true)
    .build();

  private static final String UNIVERSITY = "university";

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String STUDENT_ID = "student-id";

  private static final String MENTOR_ID = "mentor-id";

  private static final String OLD_PASSWORD = "old-password";

  private static final String NEW_PASSWORD = "new-password";

  private User userStudent;

  private User userMentor;

  @Mock
  private BatchService batchService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ResourceService resourceService;

  @Mock
  private ScoringMediatorService scoringMediatorService;

  @Mock
  private BCryptPasswordEncoder encoder;

  @InjectMocks
  private UserServiceImpl userService;

  @Before
  public void setUp() {

    userStudent = User.builder()
      .id(STUDENT_ID)
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME_STUDENT)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .batch(BATCH)
      .university(UNIVERSITY)
      .build();
    userStudent.setDeleted(false);

    userMentor = User.builder()
      .id(MENTOR_ID)
      .role(Role.MENTOR)
      .email(EMAIL_MENTOR)
      .name(NAME_MENTOR)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .build();
    userMentor.setDeleted(false);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      batchService, userRepository, resourceService, scoringMediatorService,
      encoder
    );
  }

  @Test
  public void testGivenStudentEmailByGettingUserByEmailReturnStudent() {

    when(userRepository.findOne(STUDENT_ID)).thenReturn(userStudent);

    User foundUserStudent = userService.getUser(STUDENT_ID);

    assertThat(foundUserStudent).isNotNull();
    assertThat(foundUserStudent).isEqualTo(userStudent);

    verify(userRepository).findOne(STUDENT_ID);
    verifyZeroInteractions(batchService, resourceService, encoder);
  }

  @Test
  public void testGivenMentorEmailByGettingUserByEmailReturnMentor() {

    when(userRepository.findOne(MENTOR_ID)).thenReturn(userMentor);

    User foundUserMentor = userService.getUser(MENTOR_ID);

    assertThat(foundUserMentor).isNotNull();
    assertThat(foundUserMentor).isEqualTo(userMentor);

    verify(userRepository).findOne(MENTOR_ID);
    verifyZeroInteractions(batchService, resourceService, encoder);
  }

  @Test
  public void testGivenEmailOfNonExistingUserByGettingUserByEmailReturnNotFoundException() {

    when(userRepository.findOne(NON_EXISTING_USER_ID)).thenReturn(null);

    catchException(() -> userService.getUser(NON_EXISTING_USER_ID));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get User Not Found");

    verify(userRepository).findOne(NON_EXISTING_USER_ID);
    verifyZeroInteractions(batchService, resourceService, encoder);
  }

  @Test
  public void testGivenRoleStudentByGettingUsersReturnStudentsPage() {

    User additionalUser = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME_STUDENT)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .pictureV2(PICTURE)
      .batch(BATCH)
      .university(UNIVERSITY)
      .build();
    additionalUser.setDeleted(false);

    List<User> studentsList = Arrays.asList(userStudent, additionalUser);

    when(userRepository.findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
      Role.STUDENT, "", PAGEABLE)).thenReturn(
      PageHelper.toPage(studentsList, PAGEABLE));

    Page<User> foundUserStudentsPage = userService.getUsers(
      Role.STUDENT, "", PAGEABLE);

    assertThat(foundUserStudentsPage).isNotNull();
    assertThat(foundUserStudentsPage.getContent()).isEqualTo(studentsList);

    verify(
      userRepository).findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
      Role.STUDENT, "", PAGEABLE);
    verifyZeroInteractions(batchService, resourceService, encoder);
  }

  @Test
  public void testGivenBatchCodeAndRoleStudentByGettingUsersWithinBatchReturnStudentsPage() {

    User additionalUser = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME_STUDENT)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .pictureV2(PICTURE)
      .batch(BATCH)
      .university(UNIVERSITY)
      .build();
    additionalUser.setDeleted(false);

    List<User> studentsList = Arrays.asList(userStudent, additionalUser);

    when(
      userRepository.findAllByBatchAndRoleAndDeletedFalse(BATCH, Role.STUDENT,
                                                          PAGEABLE
      )).thenReturn(PageHelper.toPage(studentsList, PAGEABLE));

    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);

    Page<User> foundUserStudentsPage = userService.getStudentsWithinBatch(
      NUMBER, PAGEABLE);

    assertThat(foundUserStudentsPage).isNotNull();
    assertThat(foundUserStudentsPage.getContent()).isEqualTo(studentsList);

    verify(userRepository).findAllByBatchAndRoleAndDeletedFalse(
      BATCH, Role.STUDENT, PAGEABLE);
    verify(batchService).getBatchByCode(NUMBER);
    verifyZeroInteractions(resourceService, encoder);
  }

  @Test
  public void testGivenNullBatchCodeAndRoleStudentByGettingUsersWithinBatchReturnStudentsPage() {

    Page<User> foundUserStudentsPage = userService.getStudentsWithinBatch(
      null, PAGEABLE);

    assertThat(foundUserStudentsPage).isNotNull();
    assertThat(foundUserStudentsPage.getTotalElements()).isEqualTo(0);

    verifyZeroInteractions(
      userRepository, batchService, resourceService, encoder);
  }

  @Test
  public void testGivenRoleMentorAndNameByGettingUsersReturnMentorsPage() {

    User additionalUser = User.builder()
      .role(Role.MENTOR)
      .email(EMAIL_MENTOR)
      .name(NAME_MENTOR)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .pictureV2(PICTURE)
      .build();
    additionalUser.setDeleted(false);

    List<User> mentorsList = Arrays.asList(userMentor, additionalUser);

    String name = "MENT";
    when(userRepository.findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
      Role.MENTOR, name, PAGEABLE)).thenReturn(
      PageHelper.toPage(mentorsList, PAGEABLE));

    Page<User> foundUserMentorsPage = userService.getUsers(
      Role.MENTOR, name, PAGEABLE);

    assertThat(foundUserMentorsPage).isNotNull();
    assertThat(foundUserMentorsPage.getContent()).isEqualTo(mentorsList);

    verify(
      userRepository).findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
      Role.MENTOR, name, PAGEABLE);
    verifyZeroInteractions(batchService, resourceService, encoder);
  }

  @Test
  public void testGivenStudentDataByCreatingUserReturnStudent() {

    userStudent.setPictureV2(PICTURE);

    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    String rawPassword = userStudent.getName()
                           .toLowerCase()
                           .replace(" ", "") + "functionapp";
    when(encoder.encode(rawPassword)).thenReturn(PASSWORD);
    when(userRepository.save(userStudent)).thenReturn(userStudent);
    when(scoringMediatorService.createQuizAndAssignmentsByStudent(
      userStudent)).thenReturn(userStudent);

    User createdUserStudent = userService.createUser(userStudent);

    assertThat(createdUserStudent).isNotNull();
    assertThat(createdUserStudent.getBatch()).isNotNull();
    assertThat(createdUserStudent.getBatch()).isEqualTo(BATCH);
    assertThat(createdUserStudent.getPictureV2()).isNotNull();
    assertThat(createdUserStudent.getPictureV2()).isEqualTo(PICTURE);

    verify(batchService).getBatchByCode(NUMBER);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(PICTURE_ID);
    verify(scoringMediatorService).createQuizAndAssignmentsByStudent(
      userStudent);
    verify(encoder).encode(rawPassword);
    verify(userRepository).save(userStudent);
  }

  @Test
  public void testGivenStudentDataButErrorOccurredByCreatingUserReturnUnsupportedOperationException() {

    userStudent.setPictureV2(PICTURE);

    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    String rawPassword = userStudent.getName()
                           .toLowerCase()
                           .replace(" ", "") + "functionapp";
    when(encoder.encode(rawPassword)).thenReturn(PASSWORD);
    when(userRepository.save(userStudent)).thenReturn(null);

    catchException(() -> userService.createUser(userStudent));

    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Failed Create User");

    verify(batchService).getBatchByCode(NUMBER);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(PICTURE_ID);
    verify(encoder).encode(rawPassword);
    verify(userRepository).save(userStudent);
  }

  @Test
  public void testGivenMentorDataByCreatingUserReturnMentor() {

    userMentor.setPictureV2(PICTURE);

    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    String rawPassword = userMentor.getName()
                           .toLowerCase()
                           .replace(" ", "") + "functionapp";
    when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    when(scoringMediatorService.createQuizAndAssignmentsByStudent(
      userMentor)).thenReturn(userMentor);

    User createdUserMentor = userService.createUser(userMentor);

    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPictureV2()).isNotNull();
    assertThat(createdUserMentor.getPictureV2()).isEqualTo(PICTURE);

    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(PICTURE_ID);
    verify(scoringMediatorService).createQuizAndAssignmentsByStudent(
      userMentor);
    verify(encoder).encode(rawPassword);
    verify(userRepository).save(userMentor);
    verifyZeroInteractions(batchService);
  }

  @Test
  public void testGivenMentorDataWithoutImageByCreatingUserReturnMentor() {

    String rawPassword = userMentor.getName()
                           .toLowerCase()
                           .replace(" ", "") + "functionapp";
    when(encoder.encode(rawPassword)).thenReturn(PASSWORD);
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    when(scoringMediatorService.createQuizAndAssignmentsByStudent(
      userMentor)).thenReturn(userMentor);

    User createdUserMentor = userService.createUser(userMentor);

    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPictureV2()).isNull();

    verify(encoder).encode(rawPassword);
    verify(userRepository).save(userMentor);
    verify(scoringMediatorService).createQuizAndAssignmentsByStudent(
      userMentor);
    verifyZeroInteractions(batchService, resourceService);
  }

  @Test
  public void testGivenStudentDataByUpdatingUserReturnStudent() {

    userStudent.setPictureV2(PICTURE);

    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(userRepository.findOne(STUDENT_ID)).thenReturn(userStudent);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    String rawPassword = userStudent.getName()
                           .toLowerCase()
                           .replace(" ", "") + "functionapp";
    when(encoder.matches(rawPassword, PASSWORD)).thenReturn(true);
    when(encoder.encode(rawPassword)).thenReturn(PASSWORD);
    when(userRepository.save(userStudent)).thenReturn(userStudent);

    User updatedUserStudent = userService.updateUser(userStudent);

    assertThat(updatedUserStudent).isNotNull();
    assertThat(updatedUserStudent.getBatch()).isNotNull();
    assertThat(updatedUserStudent.getBatch()).isEqualTo(BATCH);
    assertThat(updatedUserStudent.getPictureV2()).isNotNull();
    assertThat(updatedUserStudent.getPictureV2()).isEqualTo(PICTURE);

    verify(batchService).getBatchByCode(NUMBER);
    verify(userRepository).findOne(STUDENT_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService, times(2)).getFile(PICTURE_ID);
    verify(encoder).matches(rawPassword, PASSWORD);
    verify(encoder).encode(rawPassword);
    verify(userRepository).save(userStudent);
  }

  @Test
  public void testGivenMentorDataByUpdatingUserReturnMentor() {

    userMentor.setPictureV2(PICTURE);

    when(userRepository.findOne(MENTOR_ID)).thenReturn(userMentor);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    String rawPassword = userMentor.getName()
                           .toLowerCase()
                           .replace(" ", "") + "functionapp";
    when(encoder.matches(rawPassword, PASSWORD)).thenReturn(false);
    when(userRepository.save(userMentor)).thenReturn(userMentor);

    User updatedUserMentor = userService.updateUser(userMentor);

    assertThat(updatedUserMentor).isNotNull();
    assertThat(updatedUserMentor.getPictureV2()).isNotNull();
    assertThat(updatedUserMentor.getPictureV2()).isEqualTo(PICTURE);

    verify(userRepository).findOne(MENTOR_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService, times(2)).getFile(PICTURE_ID);
    verify(encoder).matches(rawPassword, PASSWORD);
    verify(userRepository).save(userMentor);
    verifyZeroInteractions(batchService);
  }

  @Test
  public void testGivenStudentEmailByDeletingUsersReturnSuccessfulDeletion() {

    userStudent.setPictureV2(PICTURE);

    when(userRepository.findOne(STUDENT_ID)).thenReturn(userStudent);

    User deletedUserStudent = new User();
    BeanUtils.copyProperties(userStudent, deletedUserStudent);
    deletedUserStudent.setDeleted(true);
    when(userRepository.save(deletedUserStudent)).thenReturn(
      deletedUserStudent);

    userService.deleteUser(STUDENT_ID);
    User markedDeletedUserStudent = userService.getUser(STUDENT_ID);

    assertThat(markedDeletedUserStudent).isNotNull();
    assertThat(markedDeletedUserStudent.isDeleted()).isTrue();

    verify(userRepository, times(2)).findOne(STUDENT_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).getFile(PICTURE_ID);
    verify(userRepository).save(markedDeletedUserStudent);
    verifyZeroInteractions(batchService, encoder);
  }

  @Test
  public void testGivenMentorEmailByDeletingUsersReturnSuccessfulDeletion() {

    userMentor.setPictureV2(PICTURE);

    when(userRepository.findOne(MENTOR_ID)).thenReturn(userMentor);

    User deletedUserMentor = new User();
    BeanUtils.copyProperties(userMentor, deletedUserMentor);
    deletedUserMentor.setDeleted(true);
    when(userRepository.save(deletedUserMentor)).thenReturn(deletedUserMentor);

    userService.deleteUser(MENTOR_ID);
    User markedDeletedUserMentor = userService.getUser(MENTOR_ID);

    assertThat(markedDeletedUserMentor).isNotNull();
    assertThat(markedDeletedUserMentor.isDeleted()).isTrue();

    verify(userRepository, times(2)).findOne(MENTOR_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).getFile(PICTURE_ID);
    verify(userRepository).save(markedDeletedUserMentor);
    verifyZeroInteractions(batchService, encoder);
  }

  @Test
  public void testGivenBatchCodeByGettingStudentsByBatchCodeReturnListOfStudents() {

    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(userRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT,
                                                             BATCH
    )).thenReturn(Collections.singletonList(userStudent));

    List<User> foundStudents = userService.getStudentsByBatchCode(NUMBER);

    assertThat(foundStudents).isNotNull();
    assertThat(foundStudents).isNotEmpty();
    assertThat(foundStudents.size()).isEqualTo(1);
    assertThat(foundStudents.get(0)).isEqualTo(userStudent);

    verify(batchService).getBatchByCode(NUMBER);
    verify(userRepository).findAllByRoleAndBatchAndDeletedFalse(
      Role.STUDENT, BATCH);
    verifyZeroInteractions(resourceService, encoder);
  }

  @Test
  public void testGivenNullBatchCodeByGettingStudentsByBatchCodeReturnEmptyList() {

    List<User> foundStudents = userService.getStudentsByBatchCode(null);

    assertThat(foundStudents).isNotNull();
    assertThat(foundStudents).isEmpty();

    verifyZeroInteractions(
      batchService, userRepository, resourceService, encoder);
  }

  @Test
  public void testGivenNonExistingBatchCodeByGettingStudentsByBatchCodeReturnNotFoundException() {

    when(batchService.getBatchByCode(NUMBER)).thenThrow(
      new NotFoundException(""));

    catchException(() -> userService.getStudentsByBatchCode(NUMBER));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);

    verify(batchService).getBatchByCode(NUMBER);
    verifyZeroInteractions(userRepository, resourceService, encoder);
  }

  @Test
  public void testGivenBatchCodeWithNoStudentRegisteredForThatCodeByGettingStudentsByBatchCodeReturnEmptyList() {

    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(userRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT,
                                                             BATCH
    )).thenReturn(Collections.emptyList());

    List<User> foundStudents = userService.getStudentsByBatchCode(NUMBER);

    assertThat(foundStudents).isNotNull();
    assertThat(foundStudents).isEmpty();

    verify(batchService).getBatchByCode(NUMBER);
    verify(userRepository).findAllByRoleAndBatchAndDeletedFalse(
      Role.STUDENT, BATCH);
    verifyZeroInteractions(resourceService, encoder);
  }

  @Test
  public void testGivenEmailByGettingUserByEmailReturnUser() {

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));

    User retrievedUser = userService.getUserByEmail(EMAIL_MENTOR);

    assertThat(retrievedUser).isNotNull();
    assertThat(retrievedUser).isEqualTo(userMentor);

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_MENTOR);
    verifyZeroInteractions(resourceService, encoder);
  }

  @Test
  public void testGivenNonExistingEmailByGettingUserByEmailReturnNotFoundException() {

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_MENTOR)).thenReturn(
      Optional.empty());

    catchException(() -> userService.getUserByEmail(EMAIL_MENTOR));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get User Not Found");

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_MENTOR);
    verifyZeroInteractions(resourceService, encoder);
  }

  @Test
  public void testGivenEmailAndPasswordByGettingUserByEmailAndPasswordReturnUser() {

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));

    String rawPassword = "pass";
    when(encoder.matches(rawPassword, PASSWORD)).thenReturn(true);

    User retrievedUser = userService.getUserByEmailAndPassword(
      EMAIL_STUDENT, rawPassword);

    assertThat(retrievedUser).isNotNull();
    assertThat(retrievedUser).isEqualTo(userStudent);

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_STUDENT);
    verify(encoder).matches(rawPassword, PASSWORD);
    verifyZeroInteractions(resourceService);
  }

  @Test
  public void testGivenEmailAndPasswordByGettingUserByEmailAndPasswordReturnUnauthorizedException() {

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));

    String rawPassword = "pass";
    when(encoder.matches(rawPassword, PASSWORD)).thenReturn(false);

    catchException(
      () -> userService.getUserByEmailAndPassword(EMAIL_STUDENT, rawPassword));

    assertThat(caughtException().getClass()).isEqualTo(
      UnauthorizedException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Invalid Email/Password");

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_STUDENT);
    verify(encoder).matches(rawPassword, PASSWORD);
    verifyZeroInteractions(resourceService);
  }

  @Test
  public void testGivenEmailAndPasswordByChangingUserPasswordReturnSuccessfulChange() {

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));

    when(encoder.matches(OLD_PASSWORD, userStudent.getPassword())).thenReturn(
      true);

    when(encoder.encode(NEW_PASSWORD)).thenReturn(PASSWORD);

    when(userRepository.save(userStudent)).thenReturn(userStudent);

    userService.changeUserPassword(EMAIL_STUDENT, OLD_PASSWORD, NEW_PASSWORD);

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_STUDENT);
    verify(encoder).matches(OLD_PASSWORD, userStudent.getPassword());
    verify(encoder).encode(NEW_PASSWORD);
    verify(userRepository).save(userStudent);
    verifyZeroInteractions(resourceService);
  }

  @Test
  public void testGivenEmailAndInvalidOldPasswordAndNewPasswordByChangingUserPasswordReturnUnauthorizedException() {

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));

    when(encoder.matches(OLD_PASSWORD, userStudent.getPassword())).thenReturn(
      false);

    catchException(
      () -> userService.changeUserPassword(EMAIL_STUDENT, OLD_PASSWORD,
                                           NEW_PASSWORD
      ));

    assertThat(caughtException().getClass()).isEqualTo(
      UnauthorizedException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Invalid Old Password");

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_STUDENT);
    verify(encoder).matches(OLD_PASSWORD, userStudent.getPassword());
    verifyZeroInteractions(resourceService);
  }

  @Test
  public void testGivenNameByGettingUsersByNameContainsIgnoreCaseReturnListOfUsers() {

    String namePart = "AM";
    List<User> users = Arrays.asList(userStudent, userMentor);
    when(userRepository.findAllByNameContainsIgnoreCaseAndDeletedFalse(namePart,
                                                                       PAGEABLE
    )).thenReturn(new PageImpl<>(users, PAGEABLE, users.size()));

    Page<User> retrievedUsers = userService.getUsersByNameContainsIgnoreCase(
      namePart, PAGEABLE);

    assertThat(retrievedUsers.getContent()).isNotEmpty();
    assertThat(retrievedUsers.getContent()).isEqualTo(users);

    verify(userRepository).findAllByNameContainsIgnoreCaseAndDeletedFalse(
      namePart, PAGEABLE);
    verifyZeroInteractions(resourceService, encoder);
  }

  @Test
  public void testGivenUserObjectByChangingProfilePictureReturnUpdatedUser() {

    FileV2 picture = FileV2.builder()
      .id(PICTURE_ID)
      .build();
    User user = User.builder()
      .email(EMAIL_STUDENT)
      .pictureV2(picture)
      .build();

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));

    when(resourceService.markFilesUsed(Collections.singletonList(PICTURE_ID),
                                       true
    )).thenReturn(true);

    when(resourceService.getFile(PICTURE_ID)).thenReturn(picture);

    User savedUser = new User();
    BeanUtils.copyProperties(userStudent, savedUser);
    savedUser.setPictureV2(picture);
    when(userRepository.save(savedUser)).thenReturn(savedUser);

    User updatedUser = userService.changeProfilePicture(user);

    assertThat(updatedUser).isEqualTo(savedUser);

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_STUDENT);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(PICTURE_ID), true);
    verify(resourceService).getFile(PICTURE_ID);
    verify(userRepository).save(savedUser);
  }

  @Test
  public void testGivenUserWithNonExistingEmailByChangingProfilePictureReturnRequestUserObject() {

    FileV2 picture = FileV2.builder()
      .id(PICTURE_ID)
      .build();
    User user = User.builder()
      .email(EMAIL_STUDENT)
      .pictureV2(picture)
      .build();

    when(userRepository.findByEmailAndDeletedFalse(EMAIL_STUDENT)).thenReturn(
      Optional.empty());

    User updatedUser = userService.changeProfilePicture(user);

    assertThat(updatedUser).isEqualTo(user);

    verify(userRepository).findByEmailAndDeletedFalse(EMAIL_STUDENT);
    verifyZeroInteractions(resourceService);
  }

}
