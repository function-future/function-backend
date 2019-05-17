package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
  
  private static final String ADDRESS = "address";
  
  private static final String EMAIL_MENTOR = "mentor@test.com";
  
  private static final String EMAIL_STUDENT = "student@test.com";
  
  private static final String NAME = "test-name";
  
  private static final String PASSWORD = "password";
  
  private static final String NON_EXISTING_USER_ID = "non-existing-user-id";
  
  private static final String NUMBER = "1";
  
  private static final Batch BATCH = Batch.builder()
    .code(NUMBER)
    .build();
  
  private static final String PHONE = "phone";
  
  private static final String PICTURE_ID = "picture-id";
  
  public static final List<String> FILE_IDS = Collections.singletonList(
    PICTURE_ID);
  
  private static final FileV2 PICTURE = FileV2.builder()
    .id(PICTURE_ID)
    .asResource(true)
    .build();
  
  private static final String UNIVERSITY = "university";
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  private static final String STUDENT_ID = "student-id";
  
  private static final String MENTOR_ID = "mentor-id";
  
  private User userStudent;
  
  private User userMentor;
  
  @Mock
  private BatchService batchService;
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private ResourceService resourceService;
  
  @InjectMocks
  private UserServiceImpl userService;
  
  @Before
  public void setUp() {
    
    userStudent = User.builder()
      .id(STUDENT_ID)
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME)
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
      .name(NAME)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .build();
    userMentor.setDeleted(false);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(batchService, userRepository, resourceService);
  }
  
  @Test
  public void testGivenStudentEmailByGettingUserByEmailReturnStudent() {
    
    when(userRepository.findOne(STUDENT_ID)).thenReturn(userStudent);
    
    User foundUserStudent = userService.getUser(STUDENT_ID);
    
    assertThat(foundUserStudent).isNotNull();
    assertThat(foundUserStudent).isEqualTo(userStudent);
    
    verify(userRepository).findOne(STUDENT_ID);
  }
  
  @Test
  public void testGivenMentorEmailByGettingUserByEmailReturnMentor() {
    
    when(userRepository.findOne(MENTOR_ID)).thenReturn(userMentor);
    
    User foundUserMentor = userService.getUser(MENTOR_ID);
    
    assertThat(foundUserMentor).isNotNull();
    assertThat(foundUserMentor).isEqualTo(userMentor);
    
    verify(userRepository).findOne(MENTOR_ID);
  }
  
  @Test
  public void testGivenEmailOfNonExistingUserByGettingUserByEmailReturnNotFoundException() {
    
    when(userRepository.findOne(NON_EXISTING_USER_ID)).thenReturn(null);
    
    catchException(() -> userService.getUser(NON_EXISTING_USER_ID));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get User Not Found");
    
    verify(userRepository).findOne(NON_EXISTING_USER_ID);
  }
  
  @Test
  public void testGivenRoleStudentByGettingUsersReturnStudentsPage() {
    
    User additionalUser = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .pictureV2(PICTURE)
      .batch(BATCH)
      .university(UNIVERSITY)
      .build();
    additionalUser.setDeleted(false);
    
    List<User> studentsList = Arrays.asList(userStudent, additionalUser);
    
    when(userRepository.findAllByRole(Role.STUDENT, PAGEABLE)).thenReturn(
      new PageImpl<>(studentsList, PAGEABLE, studentsList.size()));
    
    Page<User> foundUserStudentsPage = userService.getUsers(
      Role.STUDENT, PAGEABLE);
    
    assertThat(foundUserStudentsPage).isNotNull();
    assertThat(foundUserStudentsPage.getContent()).isEqualTo(studentsList);
    
    verify(userRepository).findAllByRole(Role.STUDENT, PAGEABLE);
  }
  
  @Test
  public void testGivenRoleMentorByGettingUsersReturnMentorsPage() {
    
    User additionalUser = User.builder()
      .role(Role.MENTOR)
      .email(EMAIL_MENTOR)
      .name(NAME)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .pictureV2(PICTURE)
      .build();
    additionalUser.setDeleted(false);
    
    List<User> mentorsList = Arrays.asList(userMentor, additionalUser);
    
    when(userRepository.findAllByRole(Role.MENTOR, PAGEABLE)).thenReturn(
      new PageImpl<>(mentorsList, PAGEABLE, mentorsList.size()));
    
    Page<User> foundUserMentorsPage = userService.getUsers(
      Role.MENTOR, PAGEABLE);
    
    assertThat(foundUserMentorsPage).isNotNull();
    assertThat(foundUserMentorsPage.getContent()).isEqualTo(mentorsList);
    
    verify(userRepository).findAllByRole(Role.MENTOR, PAGEABLE);
  }
  
  @Test
  public void testGivenStudentDataByCreatingUserReturnStudent() {
    
    userStudent.setPictureV2(PICTURE);
    
    when(userRepository.findOne(STUDENT_ID)).thenReturn(null);
    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    when(userRepository.save(userStudent)).thenReturn(userStudent);
    
    User createdUserStudent = userService.createUser(userStudent);
    
    assertThat(createdUserStudent).isNotNull();
    assertThat(createdUserStudent.getBatch()).isNotNull();
    assertThat(createdUserStudent.getBatch()).isEqualTo(BATCH);
    assertThat(createdUserStudent.getPictureV2()).isNotNull();
    assertThat(createdUserStudent.getPictureV2()).isEqualTo(PICTURE);
    
    verify(userRepository).findOne(STUDENT_ID);
    verify(batchService).getBatchByCode(NUMBER);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(PICTURE_ID);
    verify(userRepository).save(userStudent);
  }
  
  @Test
  public void testGivenMentorDataByCreatingUserReturnMentor() {
    
    userMentor.setPictureV2(PICTURE);
    
    when(userRepository.findOne(MENTOR_ID)).thenReturn(null);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    
    User createdUserMentor = userService.createUser(userMentor);
    
    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPictureV2()).isNotNull();
    assertThat(createdUserMentor.getPictureV2()).isEqualTo(PICTURE);
    
    verify(userRepository).findOne(MENTOR_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService).getFile(PICTURE_ID);
    verify(userRepository).save(userMentor);
  }
  
  @Test
  public void testGivenMentorDataWithoutImageByCreatingUserReturnMentor() {
    
    when(userRepository.findOne(MENTOR_ID)).thenReturn(null);
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    
    User createdUserMentor = userService.createUser(userMentor);
    
    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPictureV2()).isNull();
    
    verify(userRepository).findOne(MENTOR_ID);
    verify(userRepository).save(userMentor);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenDeletedStudentDataByCreatingUserReturnStudent() {
    
    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    
    userStudent.setDeleted(true);
    
    when(userRepository.findOne(STUDENT_ID)).thenReturn(userStudent);
    
    User savedUserStudent;
    BeanUtils.copyProperties(userStudent, savedUserStudent = new User());
    savedUserStudent.setDeleted(false);
    when(userRepository.save(savedUserStudent)).thenReturn(savedUserStudent);
    
    User createdUserStudent = userService.createUser(userStudent);
    
    assertThat(createdUserStudent).isNotNull();
    
    verify(batchService).getBatchByCode(NUMBER);
    verify(userRepository).findOne(STUDENT_ID);
    verify(userRepository, times(2)).save(savedUserStudent);
  }
  
  @Test
  public void testGivenDeletedMentorDataByCreatingUserReturnMentor() {
    
    userMentor.setDeleted(true);
    
    when(userRepository.findOne(MENTOR_ID)).thenReturn(userMentor);
    
    User savedUserMentor;
    BeanUtils.copyProperties(userMentor, savedUserMentor = new User());
    savedUserMentor.setDeleted(false);
    when(userRepository.save(savedUserMentor)).thenReturn(savedUserMentor);
    
    User createdUserMentor = userService.createUser(userMentor);
    
    assertThat(createdUserMentor).isNotNull();
    
    verify(userRepository).findOne(MENTOR_ID);
    verify(userRepository, times(2)).save(savedUserMentor);
  }
  
  @Test
  public void testGivenStudentDataByUpdatingUserReturnStudent() {
    
    userStudent.setPictureV2(PICTURE);
    
    when(batchService.getBatchByCode(NUMBER)).thenReturn(BATCH);
    when(userRepository.findOne(STUDENT_ID)).thenReturn(userStudent);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
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
    verify(userRepository).save(userStudent);
  }
  
  @Test
  public void testGivenMentorDataByUpdatingUserReturnMentor() {
    
    userMentor.setPictureV2(PICTURE);
    
    when(userRepository.findOne(MENTOR_ID)).thenReturn(userMentor);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(resourceService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    
    User updatedUserMentor = userService.updateUser(userMentor);
    
    assertThat(updatedUserMentor).isNotNull();
    assertThat(updatedUserMentor.getPictureV2()).isNotNull();
    assertThat(updatedUserMentor.getPictureV2()).isEqualTo(PICTURE);
    
    verify(userRepository).findOne(MENTOR_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(resourceService, times(2)).getFile(PICTURE_ID);
    verify(userRepository).save(userMentor);
  }
  
  @Test
  public void testGivenStudentEmailByDeletingUsersReturnSuccessfulDeletion() {
    
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
    verify(userRepository).save(markedDeletedUserStudent);
  }
  
  @Test
  public void testGivenMentorEmailByDeletingUsersReturnSuccessfulDeletion() {
    
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
    verify(userRepository).save(markedDeletedUserMentor);
  }
  
}
