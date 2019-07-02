package com.future.function.web.mapper.request.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.UserWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRequestMapperTest {
  
  private static final String ADDRESS = "address";
  
  private static final String ADMIN_EMAIL = "admin@test.com";
  
  private static final String NAME = "name";
  
  private static final String PASSWORD = "namefunctionapp";
  
  private static final String NUMBER = "1";
  
  private static final String PHONE = "081212341234";
  
  private static final String STUDENT_EMAIL = "student@test.com";
  
  private static final String UNIVERSITY = "university";
  
  private static final String STUDENT_ID = "student-id";
  
  private static final String FILE_ID = "file-id";
  
  private static final User STUDENT_WITH_ID = User.builder()
    .id(STUDENT_ID)
    .role(Role.STUDENT)
    .email(STUDENT_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .pictureV2(FileV2.builder()
                 .id(FILE_ID)
                 .build())
    .batch(Batch.builder()
             .code(NUMBER)
             .build())
    .university(UNIVERSITY)
    .build();
  
  private static final User STUDENT = User.builder()
    .role(Role.STUDENT)
    .email(STUDENT_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .pictureV2(FileV2.builder()
                 .id(FILE_ID)
                 .build())
    .batch(Batch.builder()
             .code(NUMBER)
             .build())
    .university(UNIVERSITY)
    .build();
  
  private static final UserWebRequest STUDENT_WEB_REQUEST =
    UserWebRequest.builder()
      .role(Role.STUDENT.name())
      .email(STUDENT_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .batch(NUMBER)
      .university(UNIVERSITY)
      .avatar(Collections.singletonList(FILE_ID))
      .build();
  
  private static final String VALID_ADMIN_ID = "valid-admin-id";
  
  private static final User VALID_ADMIN_WITH_ID = User.builder()
    .id(VALID_ADMIN_ID)
    .role(Role.ADMIN)
    .email(ADMIN_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .pictureV2(FileV2.builder()
                 .id(FILE_ID)
                 .build())
    .build();
  
  private static final User VALID_ADMIN = User.builder()
    .role(Role.ADMIN)
    .email(ADMIN_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .pictureV2(FileV2.builder()
                 .id(FILE_ID)
                 .build())
    .build();
  
  private static final UserWebRequest VALID_ADMIN_WEB_REQUEST =
    UserWebRequest.builder()
      .role(Role.ADMIN.name())
      .email(ADMIN_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .avatar(Collections.singletonList(FILE_ID))
      .build();
  
  private static final User VALID_ADMIN_WITH_EMPTY_AVATAR = User.builder()
    .role(Role.ADMIN)
    .email(ADMIN_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .pictureV2(null)
    .build();
  
  private static final UserWebRequest
    VALID_ADMIN_WEB_REQUEST_WITH_EMPTY_AVATAR = UserWebRequest.builder()
    .role(Role.ADMIN.name())
    .email(ADMIN_EMAIL)
    .name(NAME)
    .phone(PHONE)
    .address(ADDRESS)
    .avatar(Collections.emptyList())
    .build();
  
  @InjectMocks
  private UserRequestMapper userRequestMapper;
  
  @Mock
  private RequestValidator validator;
  
  @Before
  public void setUp() {
    
    when(validator.validate(STUDENT_WEB_REQUEST)).thenReturn(
      STUDENT_WEB_REQUEST);
    when(validator.validate(VALID_ADMIN_WEB_REQUEST)).thenReturn(
      VALID_ADMIN_WEB_REQUEST);
    when(
      validator.validate(VALID_ADMIN_WEB_REQUEST_WITH_EMPTY_AVATAR)).thenReturn(
      VALID_ADMIN_WEB_REQUEST_WITH_EMPTY_AVATAR);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenJsonDataAsObjectByParsingToUserClassReturnUserObject() {
    
    User parsedStudent = userRequestMapper.toUser(STUDENT_WEB_REQUEST);
    
    assertThat(parsedStudent).isEqualTo(STUDENT);
    
    User parsedAdmin = userRequestMapper.toUser(VALID_ADMIN_WEB_REQUEST);
    
    assertThat(parsedAdmin).isEqualTo(VALID_ADMIN);
    
    User parsedAdminWithEmptyAvatar = userRequestMapper.toUser(
      VALID_ADMIN_WEB_REQUEST_WITH_EMPTY_AVATAR);
    
    assertThat(parsedAdminWithEmptyAvatar).isEqualTo(
      VALID_ADMIN_WITH_EMPTY_AVATAR);
    
    verify(validator).validate(STUDENT_WEB_REQUEST);
    verify(validator).validate(VALID_ADMIN_WEB_REQUEST);
    verify(validator).validate(VALID_ADMIN_WEB_REQUEST_WITH_EMPTY_AVATAR);
  }
  
  @Test
  public void testGivenUserIdAndJsonDataAsObjectByParsingToUserClassReturnUserObject() {
    
    User parsedStudent = userRequestMapper.toUser(
      STUDENT_ID, STUDENT_WEB_REQUEST);
    
    assertThat(parsedStudent).isEqualTo(STUDENT_WITH_ID);
    
    User parsedAdmin = userRequestMapper.toUser(
      VALID_ADMIN_ID, VALID_ADMIN_WEB_REQUEST);
    
    assertThat(parsedAdmin).isEqualTo(VALID_ADMIN_WITH_ID);
    
    verify(validator).validate(STUDENT_WEB_REQUEST);
    verify(validator).validate(VALID_ADMIN_WEB_REQUEST);
  }
  
}
