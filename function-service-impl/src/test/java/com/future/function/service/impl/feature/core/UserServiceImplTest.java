package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.FileService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
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
  
  private static final String PASSWORD = "password";
  
  private static final String NON_EXISTING_USER_EMAIL = "email@email.com";
  
  private static final Long NUMBER = 1L;
  
  private static final Batch BATCH = Batch.builder()
    .code(NUMBER)
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
  
  @Mock
  private ResourceLoader resourceLoader;
  
  @InjectMocks
  private UserServiceImpl userService;
  
  @Before
  public void setUp() {
    
    userStudent = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_STUDENT)
      .name(NAME)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .picture(PICTURE)
      .batch(BATCH)
      .university(UNIVERSITY)
      .build();
    userStudent.setDeleted(false);
    
    userMentor = User.builder()
      .role(Role.MENTOR)
      .email(EMAIL_MENTOR)
      .name(NAME)
      .password(PASSWORD)
      .phone(PHONE)
      .address(ADDRESS)
      .picture(PICTURE)
      .build();
    userMentor.setDeleted(false);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(
      batchService, fileService, userRepository, resourceLoader);
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
    
    catchException(() -> userService.getUser(NON_EXISTING_USER_EMAIL));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get User Not Found");
    
    verify(userRepository).findByEmail(NON_EXISTING_USER_EMAIL);
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
      .picture(PICTURE)
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
      .picture(PICTURE)
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
    
    when(userRepository.findByEmail(EMAIL_STUDENT)).thenReturn(
      Optional.empty());
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
    
    verify(userRepository).findByEmail(EMAIL_STUDENT);
    verify(batchService).getBatch(NUMBER);
    verify(fileService).storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER);
    verify(fileService).getFile(PICTURE_ID);
    verify(userRepository).save(userStudent);
  }
  
  @Test
  public void testGivenMentorDataByCreatingUserReturnMentor() {
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(Optional.empty());
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    when(fileService.storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER)).thenReturn(
      PICTURE);
    when(fileService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    
    User createdUserMentor = userService.createUser(
      userMentor, MOCK_MULTIPARTFILE);
    
    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPicture()).isNotNull();
    assertThat(createdUserMentor.getPicture()).isEqualTo(PICTURE);
    
    verify(userRepository).findByEmail(EMAIL_MENTOR);
    verify(userRepository).save(userMentor);
    verify(fileService).storeFile(MOCK_MULTIPARTFILE, FileOrigin.USER);
    verify(fileService).getFile(PICTURE_ID);
  }
  
  @Test
  public void testGivenMentorDataWithoutImageByCreatingUserReturnMentor() {
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(Optional.empty());
    when(userRepository.save(userMentor)).thenReturn(userMentor);
    when(
      resourceLoader.getResource("classpath:default-profile.png")).thenReturn(
      new ClassPathResource("default-profile.png"));
    when(fileService.storeFile(any(MultipartFile.class),
                               any(FileOrigin.class)
    )).thenReturn(PICTURE);
    when(fileService.getFile(PICTURE_ID)).thenReturn(PICTURE);
    
    User createdUserMentor = userService.createUser(userMentor, null);
    
    assertThat(createdUserMentor).isNotNull();
    assertThat(createdUserMentor.getPicture()).isNotNull();
    assertThat(createdUserMentor.getPicture()).isEqualTo(PICTURE);
    
    verify(userRepository).findByEmail(EMAIL_MENTOR);
    verify(userRepository).save(userMentor);
    verify(resourceLoader).getResource("classpath:default-profile.png");
    verify(fileService).storeFile(
      any(MultipartFile.class), any(FileOrigin.class));
    verify(fileService).getFile(PICTURE_ID);
  }
  
  @Test
  public void testGivenDeletedStudentDataByCreatingUserReturnStudent() {
    
    when(batchService.getBatch(NUMBER)).thenReturn(BATCH);
    
    userStudent.setDeleted(true);
    
    when(userRepository.findByEmail(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));
    
    User savedUserStudent;
    BeanUtils.copyProperties(userStudent, savedUserStudent = new User());
    savedUserStudent.setDeleted(false);
    when(userRepository.save(savedUserStudent)).thenReturn(savedUserStudent);
    
    User createdUserStudent = userService.createUser(
      userStudent, MOCK_MULTIPARTFILE);
    
    assertThat(createdUserStudent).isNotNull();
    
    verify(batchService).getBatch(NUMBER);
    verify(userRepository).findByEmail(EMAIL_STUDENT);
    verify(userRepository, times(2)).save(savedUserStudent);
  }
  
  @Test
  public void testGivenDeletedMentorDataByCreatingUserReturnMentor() {
    
    userMentor.setDeleted(true);
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));
    
    User savedUserMentor;
    BeanUtils.copyProperties(userMentor, savedUserMentor = new User());
    savedUserMentor.setDeleted(false);
    when(userRepository.save(savedUserMentor)).thenReturn(savedUserMentor);
    
    User createdUserMentor = userService.createUser(
      userMentor, MOCK_MULTIPARTFILE);
    
    assertThat(createdUserMentor).isNotNull();
    
    verify(userRepository).findByEmail(EMAIL_MENTOR);
    verify(userRepository, times(2)).save(savedUserMentor);
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
    
    User deletedUserStudent = new User();
    BeanUtils.copyProperties(userStudent, deletedUserStudent);
    deletedUserStudent.setDeleted(true);
    when(userRepository.save(deletedUserStudent)).thenReturn(
      deletedUserStudent);
    
    userService.deleteUser(EMAIL_STUDENT);
    User markedDeletedUserStudent = userService.getUser(EMAIL_STUDENT);
    
    assertThat(markedDeletedUserStudent).isNotNull();
    assertThat(markedDeletedUserStudent.isDeleted()).isTrue();
    
    verify(userRepository, times(2)).findByEmail(EMAIL_STUDENT);
    verify(userRepository).save(markedDeletedUserStudent);
  }
  
  @Test
  public void testGivenMentorEmailByDeletingUsersReturnSuccessfulDeletion() {
    
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));
    
    User deletedUserMentor = new User();
    BeanUtils.copyProperties(userMentor, deletedUserMentor);
    deletedUserMentor.setDeleted(true);
    when(userRepository.save(deletedUserMentor)).thenReturn(deletedUserMentor);
    
    userService.deleteUser(EMAIL_MENTOR);
    User markedDeletedUserMentor = userService.getUser(EMAIL_MENTOR);
    
    assertThat(markedDeletedUserMentor).isNotNull();
    assertThat(markedDeletedUserMentor.isDeleted()).isTrue();
    
    verify(userRepository, times(2)).findByEmail(EMAIL_MENTOR);
    verify(userRepository).save(markedDeletedUserMentor);
  }
  
  @Test
  public void testGivenEmailOfNonExistingUserByDeletingUserReturnNotFoundException() {
    
    when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL)).thenReturn(
      Optional.empty());
    
    catchException(() -> userService.deleteUser(NON_EXISTING_USER_EMAIL));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get User Not Found");
    
    verify(userRepository).findByEmail(NON_EXISTING_USER_EMAIL);
  }
  
}
