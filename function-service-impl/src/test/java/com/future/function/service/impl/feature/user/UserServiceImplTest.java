package com.future.function.service.impl.feature.user;

import com.future.function.common.enumeration.FileOrigin;
import com.future.function.common.enumeration.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.file.File;
import com.future.function.model.entity.feature.user.User;
import com.future.function.repository.feature.user.UserRepository;
import com.future.function.service.api.feature.batch.BatchService;
import com.future.function.service.api.feature.file.FileService;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
  
  private static final String ADDRESS = "address";
  
  private static final String EMAIL_MENTOR = "mentor@test.com";
  
  private static final String EMAIL_STUDENT = "student@test.com";
  
  private static final String NAME = "test-name";
  
  private static final String NON_EXISTING_USER_EMAIL = "email@email.com";
  
  private static final Long NUMBER = 1L;
  
  private static final Batch BATCH = Batch.builder()
    .number(NUMBER)
    .build();
  
  private static final String PHONE = "phone";
  
  private static final String PICTURE_ID = "picture-id";
  
  private static final File PICTURE = File.builder()
    .id(PICTURE_ID)
    .asResource(true)
    .build();
  
  private static final String UNIVERSITY = "university";
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  private static final MockMultipartFile MOCK_MULTIPARTFILE =
    new MockMultipartFile("mock", "mock.png", "image/png", new byte[] {});
  
  private User userStudent;
  
  private User userMentor;
  
  @Mock
  private BatchService batchService;
  
  @Mock
  private FileService fileService;
  
  @Mock
  private UserRepository userRepository;
  
  @InjectMocks
  private UserServiceImpl userService;
  
  @Before
  public void setUp() {
    
    userStudent = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .picture(PICTURE)
      .batch(BATCH)
      .university(UNIVERSITY)
      .deleted(false)
      .build();
    
    userMentor = User.builder()
      .role(Role.MENTOR)
      .email(EMAIL_MENTOR)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .picture(PICTURE)
      .deleted(false)
      .build();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(batchService, fileService, userRepository);
  }
  
  @Test
  public void testGivenStudentEmailByGettingUserByEmailReturnStudent() {
    
    when(userRepository.findByEmail(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));
    
    User foundUserStudent = userService.getUser(EMAIL_STUDENT);
    
    assertThat(foundUserStudent).isNotNull();
    assertThat(foundUserStudent).isEqualTo(userStudent);
    
    verify(userRepository).findByEmail(EMAIL_STUDENT);
  }
  
  @Test
  public void testGivenMentorEmailByGettingUserByEmailReturnMentor() {
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));
    
    User foundUserMentor = userService.getUser(EMAIL_MENTOR);
    
    assertThat(foundUserMentor).isNotNull();
    assertThat(foundUserMentor).isEqualTo(userMentor);
    
    verify(userRepository).findByEmail(EMAIL_MENTOR);
  }
  
  @Test
  public void testGivenEmailOfNonExistingUserByGettingUserByEmailReturnNotFoundException() {
    
    when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL)).thenReturn(
      Optional.empty());
    
    try {
      userService.getUser(NON_EXISTING_USER_EMAIL);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Get User Not Found");
    }
    
    verify(userRepository).findByEmail(NON_EXISTING_USER_EMAIL);
  }
  
  @Test
  public void testGivenRoleStudentByGettingUsersReturnStudentsPage() {
    
    User additionalUser = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .picture(PICTURE)
      .batch(BATCH)
      .university(UNIVERSITY)
      .deleted(false)
      .build();
    
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
      .phone(PHONE)
      .address(ADDRESS)
      .picture(PICTURE)
      .deleted(false)
      .build();
    
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
    
    userStudent.setPicture(PICTURE);
    
    when(batchService.getBatch(NUMBER)).thenReturn(BATCH);
    when(fileService.storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER)).thenReturn(
      PICTURE);
    when(fileService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    when(userRepository.save(userStudent)).thenReturn(userStudent);
    
    User createdUserStudent = userService.createUser(
      userStudent, MOCK_MULTIPARTFILE);
    
    assertThat(createdUserStudent).isNotNull();
    assertThat(createdUserStudent.getBatch()).isNotNull();
    assertThat(createdUserStudent.getBatch()).isEqualTo(BATCH);
    assertThat(createdUserStudent.getPicture()).isNotNull();
    assertThat(createdUserStudent.getPicture()).isEqualTo(PICTURE);
    
    verify(batchService).getBatch(NUMBER);
    verify(fileService).storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER);
    verify(fileService).getFile(PICTURE_ID);
    verify(userRepository).save(userStudent);
  }
  
  @Test
  public void testGivenMentorDataByCreatingUserReturnMentor() {
    
    userMentor.setPicture(PICTURE);
    
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    when(fileService.storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER)).thenReturn(
      PICTURE);
    when(fileService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    
    User createdUserMentor = userService.createUser(
      userMentor, MOCK_MULTIPARTFILE);
    
    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPicture()).isNotNull();
    assertThat(createdUserMentor.getPicture()).isEqualTo(PICTURE);
    
    verify(userRepository).save(userMentor);
    verify(fileService).storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER);
    verify(fileService).getFile(PICTURE_ID);
  }
  
  @Test
  public void testGivenStudentDataByUpdatingUserReturnStudent() {
    
    userStudent.setPicture(PICTURE);
    
    when(batchService.getBatch(NUMBER)).thenReturn(BATCH);
    when(userRepository.findByEmail(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));
    when(fileService.storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER)).thenReturn(
      PICTURE);
    when(fileService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    when(userRepository.save(userStudent)).thenReturn(userStudent);
    
    User updatedUserStudent = userService.updateUser(userStudent,
                                                     MOCK_MULTIPARTFILE
    );
    
    assertThat(updatedUserStudent).isNotNull();
    assertThat(updatedUserStudent.getBatch()).isNotNull();
    assertThat(updatedUserStudent.getBatch()).isEqualTo(BATCH);
    assertThat(updatedUserStudent.getPicture()).isNotNull();
    assertThat(updatedUserStudent.getPicture()).isEqualTo(PICTURE);
    
    verify(batchService).getBatch(NUMBER);
    verify(userRepository).findByEmail(EMAIL_STUDENT);
    verify(fileService).deleteFile(PICTURE_ID);
    verify(fileService).storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER);
    verify(fileService).getFile(PICTURE_ID);
    verify(userRepository).save(userStudent);
  }
  
  @Test
  public void testGivenMentorDataByUpdatingUserReturnMentor() {
    
    userMentor.setPicture(PICTURE);
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));
    when(fileService.storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER)).thenReturn(
      PICTURE);
    when(fileService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    
    User updatedUserMentor = userService.updateUser(
      userMentor, MOCK_MULTIPARTFILE);
    
    assertThat(updatedUserMentor).isNotNull();
    assertThat(updatedUserMentor.getPicture()).isNotNull();
    assertThat(updatedUserMentor.getPicture()).isEqualTo(PICTURE);
    
    verify(userRepository).findByEmail(EMAIL_MENTOR);
    verify(fileService).deleteFile(PICTURE_ID);
    verify(fileService).storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER);
    verify(fileService).getFile(PICTURE_ID);
    verify(userRepository).save(userMentor);
  }
  
  @Test
  public void testGivenStudentEmailByDeletingUsersReturnSuccessfulDeletion() {
    
    when(userRepository.findByEmail(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));
    
    userService.deleteUser(EMAIL_STUDENT);
    User deletedUserStudent = userService.getUser(EMAIL_STUDENT);
    
    assertThat(deletedUserStudent).isNotNull();
    assertThat(deletedUserStudent.isDeleted()).isTrue();
    
    verify(userRepository, times(2)).findByEmail(EMAIL_STUDENT);
    verify(userRepository).save(deletedUserStudent);
  }
  
  @Test
  public void testGivenMentorEmailByDeletingUsersReturnSuccessfulDeletion() {
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));
    
    userService.deleteUser(EMAIL_MENTOR);
    User deletedUserMentor = userService.getUser(EMAIL_MENTOR);
    
    assertThat(deletedUserMentor).isNotNull();
    assertThat(deletedUserMentor.isDeleted()).isTrue();
    
    verify(userRepository, times(2)).findByEmail(EMAIL_MENTOR);
    verify(userRepository).save(deletedUserMentor);
  }
  
  @Test
  public void testGivenEmailOfNonExistingUserByDeletingUserReturnNotFoundException() {
    
    when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL)).thenReturn(
      Optional.empty());
    
    try {
      userService.deleteUser(NON_EXISTING_USER_EMAIL);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Delete User Not Found");
    }
    
    verify(userRepository).findByEmail(NON_EXISTING_USER_EMAIL);
  }
  
}
