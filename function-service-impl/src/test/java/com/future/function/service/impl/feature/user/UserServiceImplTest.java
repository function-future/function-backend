package com.future.function.service.impl.feature.user;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.file.FileInfo;
import com.future.function.model.entity.feature.user.User;
import com.future.function.model.util.constant.Role;
import com.future.function.repository.feature.user.UserRepository;
import com.future.function.service.api.feature.batch.BatchService;
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
  
  private static final String NAME = "test name";
  
  private static final String NON_EXISTING_USER_EMAIL = "email@email.com";
  
  private static final Long NUMBER = 1L;
  
  private static final Batch BATCH = Batch.builder()
    .number(NUMBER)
    .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  private static final String PHONE = "phone";
  
  private static final FileInfo PICTURE = FileInfo.builder()
    .build();
  
  private static final String UNIVERSITY = "university";
  
  private User additionalUser;
  
  @Mock
  private BatchService batchService;
  
  private User userMentor;
  
  @Mock
  private UserRepository userRepository;
  
  @InjectMocks
  private UserServiceImpl userService;
  
  private User userStudent;
  
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
    
    when(batchService.getBatch(NUMBER)).thenReturn(BATCH);
    when(userRepository.findByEmail(EMAIL_MENTOR)).thenReturn(
      Optional.of(userMentor));
    when(userRepository.findByEmail(EMAIL_STUDENT)).thenReturn(
      Optional.of(userStudent));
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(userRepository);
    verifyNoMoreInteractions(batchService);
  }
  
  @Test
  public void testGivenEmailByGettingUserByEmailReturnUserObject() {
    
    User foundUserMentor = userService.getUser(EMAIL_MENTOR);
    
    assertThat(foundUserMentor).isNotNull();
    assertThat(foundUserMentor).isEqualTo(userMentor);
    
    User foundUserStudent = userService.getUser(EMAIL_STUDENT);
    
    assertThat(foundUserStudent).isNotNull();
    assertThat(foundUserStudent).isEqualTo(userStudent);
    
    verify(userRepository, times(1)).findByEmail(EMAIL_MENTOR);
    verify(userRepository, times(1)).findByEmail(EMAIL_STUDENT);
  }
  
  @Test
  public void testGivenEmailOfNonExistingUserByDeletingUserReturnRuntimeException() {
    
    when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL)).thenReturn(
      Optional.empty());
    
    try {
      userService.deleteUser(NON_EXISTING_USER_EMAIL);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Delete User Not Found");
    }
    
    verify(userRepository, times(1)).findByEmail(NON_EXISTING_USER_EMAIL);
  }
  
  @Test
  public void testGivenEmailOfExistingUserByDeletingUsersReturnSuccessfulDeletion() {
    
    userService.deleteUser(EMAIL_MENTOR);
    User deletedUserMentor = userService.getUser(EMAIL_MENTOR);
    
    assertThat(deletedUserMentor).isNotNull();
    assertThat(deletedUserMentor.isDeleted()).isTrue();
    
    userService.deleteUser(EMAIL_STUDENT);
    User deletedUserStudent = userService.getUser(EMAIL_STUDENT);
    
    assertThat(deletedUserStudent).isNotNull();
    assertThat(deletedUserStudent.isDeleted()).isTrue();
    
    verify(userRepository, times(2)).findByEmail(EMAIL_MENTOR);
    verify(userRepository, times(2)).findByEmail(EMAIL_STUDENT);
  }
  
  @Test
  public void testGivenEmailOfNonExistingUserByGettingUserByEmailReturnRuntimeException() {
    
    when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL)).thenReturn(
      Optional.empty());
    
    try {
      userService.getUser(NON_EXISTING_USER_EMAIL);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NotFoundException.class);
      assertThat(e.getMessage()).isEqualTo("Get User Not Found");
    }
    
    verify(userRepository, times(1)).findByEmail(NON_EXISTING_USER_EMAIL);
  }
  
  @Test
  public void testGivenRoleByGettingUsersReturnUsersPage() {
    
    additionalUser = User.builder()
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
    
    additionalUser = User.builder()
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
    
    verify(userRepository, times(1)).findAllByRole(Role.MENTOR, PAGEABLE);
    verify(userRepository, times(1)).findAllByRole(Role.STUDENT, PAGEABLE);
  }
  
}
