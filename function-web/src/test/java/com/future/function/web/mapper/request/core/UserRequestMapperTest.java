package com.future.function.web.mapper.request.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.UserWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRequestMapperTest {
  
  private static final String ADDRESS = "address";
  
  private static final String ADMIN_EMAIL = "admin@test.com";
  
  private static final String BAD_JSON = "{}";
  
  private static final String NAME = "name";
  
  private static final String PASSWORD = "namefunctionapp";
  
  private static final String NUMBER = "1";
  
  private static final String PHONE = "081212341234";
  
  private static final String STUDENT_EMAIL = "student@test.com";
  
  private static final String UNIVERSITY = "university";
  
  private static final String STUDENT_ID = "student-id";
  
  private static final User STUDENT_WITH_ID = User.builder()
    .id(STUDENT_ID)
    .role(Role.STUDENT)
    .email(STUDENT_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .picture(new File())
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
    .picture(new File())
    .batch(Batch.builder()
             .code(NUMBER)
             .build())
    .university(UNIVERSITY)
    .build();
  
  private static final String STUDENT_JSON =
    "{\n" + "    \"role\": \"STUDENT\",\n" + "    \"email\": \"" + ADMIN_EMAIL +
    "\",\n" + "    \"name\": \"" + NAME + "\",\n" + "    \"phone\": \"" +
    PHONE + "\",\n" + "    \"address\": \"" + ADDRESS + "\",\n" +
    "    \"batch\": " + NUMBER + ",\n" + "    \"university\": \"" + UNIVERSITY +
    "\"\n" + "}";
  
  private static final UserWebRequest STUDENT_WEB_REQUEST =
    UserWebRequest.builder()
      .role(Role.STUDENT.name())
      .email(STUDENT_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .batch(NUMBER)
      .university(UNIVERSITY)
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
    .picture(new File())
    .build();
  
  private static final User VALID_ADMIN = User.builder()
    .role(Role.ADMIN)
    .email(ADMIN_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .picture(new File())
    .build();
  
  private static final String VALID_ADMIN_JSON =
    "{\n" + "    \"role\": \"ADMIN\",\n" + "    \"email\": \"" + ADMIN_EMAIL +
    "\",\n" + "    \"name\": \"" + NAME + "\",\n" + "    \"phone\": \"" +
    PHONE + "\",\n" + "    \"address\": \"" + ADDRESS + "\"\n" + "}";
  
  private static final UserWebRequest VALID_ADMIN_WEB_REQUEST =
    UserWebRequest.builder()
      .role(Role.ADMIN.name())
      .email(ADMIN_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .build();
  
  @Mock
  private WebRequestMapper requestMapper;
  
  @InjectMocks
  private UserRequestMapper userRequestMapper;
  
  @Mock
  private ObjectValidator validator;
  
  @Before
  public void setUp() {
  
    when(requestMapper.toWebRequestObject(STUDENT_JSON,
                                          UserWebRequest.class
    )).thenReturn(STUDENT_WEB_REQUEST);
    when(requestMapper.toWebRequestObject(VALID_ADMIN_JSON, UserWebRequest.class
    )).thenReturn(VALID_ADMIN_WEB_REQUEST);
    when(requestMapper.toWebRequestObject(BAD_JSON,
                                          UserWebRequest.class
    )).thenThrow(new BadRequestException("Bad Request"));
    when(validator.validate(STUDENT_WEB_REQUEST)).thenReturn(
      STUDENT_WEB_REQUEST);
    when(validator.validate(VALID_ADMIN_WEB_REQUEST)).thenReturn(
      VALID_ADMIN_WEB_REQUEST);
  }
  
  @After
  public void tearDown() {
  
    verifyNoMoreInteractions(requestMapper, validator);
  }
  
  @Test
  public void testGivenJsonDataWithInvalidFormatAsStringByParsingToUserClassReturnBadRequestException() {
    
    catchException(() -> userRequestMapper.toUser(BAD_JSON));
  
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Bad Request");
  
    verify(requestMapper).toWebRequestObject(BAD_JSON, UserWebRequest.class);
  }
  
  @Test
  public void testGivenJsonDataAsStringByParsingToUserClassReturnUserObject() {
    
    User parsedStudent = userRequestMapper.toUser(STUDENT_JSON);
    
    assertThat(parsedStudent).isEqualTo(STUDENT);
    
    User parsedAdmin = userRequestMapper.toUser(VALID_ADMIN_JSON);
    
    assertThat(parsedAdmin).isEqualTo(VALID_ADMIN);
  
    verify(requestMapper).toWebRequestObject(
      STUDENT_JSON, UserWebRequest.class);
    verify(requestMapper).toWebRequestObject(
      VALID_ADMIN_JSON, UserWebRequest.class);
    verify(validator).validate(STUDENT_WEB_REQUEST);
    verify(validator).validate(VALID_ADMIN_WEB_REQUEST);
  }
  
  @Test
  public void testGivenUserIdAndJsonDataAsStringByParsingToUserClassReturnUserObject() {
    
    User parsedStudent = userRequestMapper.toUser(STUDENT_ID, STUDENT_JSON);
    
    assertThat(parsedStudent).isEqualTo(STUDENT_WITH_ID);
    
    User parsedAdmin = userRequestMapper.toUser(VALID_ADMIN_ID, VALID_ADMIN_JSON);
    
    assertThat(parsedAdmin).isEqualTo(VALID_ADMIN_WITH_ID);
  
    verify(requestMapper).toWebRequestObject(
      STUDENT_JSON, UserWebRequest.class);
    verify(requestMapper).toWebRequestObject(
      VALID_ADMIN_JSON, UserWebRequest.class);
    verify(validator).validate(STUDENT_WEB_REQUEST);
    verify(validator).validate(VALID_ADMIN_WEB_REQUEST);
  }
  
}
