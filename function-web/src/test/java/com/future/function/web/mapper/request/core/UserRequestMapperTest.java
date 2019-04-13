package com.future.function.web.mapper.request.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.request.core.UserWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

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
  
  private static final Long NUMBER = 1L;
  
  private static final String PHONE = "081212341234";
  
  private static final String STUDENT_EMAIL = "student@test.com";
  
  private static final String UNIVERSITY = "university";
  
  private static final User STUDENT = User.builder()
    .role(Role.STUDENT)
    .email(STUDENT_EMAIL)
    .name(NAME)
    .password(PASSWORD)
    .phone(PHONE)
    .address(ADDRESS)
    .picture(new File())
    .batch(Batch.builder()
             .number(NUMBER)
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
  private ObjectMapper objectMapper;
  
  @InjectMocks
  private UserRequestMapper userRequestMapper;
  
  @Mock
  private ObjectValidator validator;
  
  @Before
  public void setUp() throws Exception {
    
    when(objectMapper.readValue(STUDENT_JSON, UserWebRequest.class)).thenReturn(
      STUDENT_WEB_REQUEST);
    when(objectMapper.readValue(VALID_ADMIN_JSON,
                                UserWebRequest.class
    )).thenReturn(VALID_ADMIN_WEB_REQUEST);
    when(objectMapper.readValue(BAD_JSON, UserWebRequest.class)).thenThrow(
      new IOException());
    when(validator.validate(STUDENT)).thenReturn(STUDENT);
    when(validator.validate(VALID_ADMIN)).thenReturn(VALID_ADMIN);
  }
  
  @After
  public void tearDown() {
  
    verifyNoMoreInteractions(objectMapper, validator);
  }
  
  @Test
  public void testGivenJsonDataWithInvalidFormatAsStringByParsingToUserClassReturnBadRequestException()
    throws Exception {
    
    try {
      userRequestMapper.toUser(BAD_JSON);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(BadRequestException.class);
      assertThat(e.getMessage()).isEqualTo("Bad Request");
    }
    
    verify(objectMapper).readValue(BAD_JSON, UserWebRequest.class);
  }
  
  @Test
  public void testGivenJsonDataAsStringByParsingToUserClassReturnUserObject()
    throws Exception {
    
    User parsedStudent = userRequestMapper.toUser(STUDENT_JSON);
    
    assertThat(parsedStudent).isEqualTo(STUDENT);
    
    User parsedAdmin = userRequestMapper.toUser(VALID_ADMIN_JSON);
    
    assertThat(parsedAdmin).isEqualTo(VALID_ADMIN);
  
    verify(objectMapper).readValue(STUDENT_JSON, UserWebRequest.class);
    verify(objectMapper).readValue(VALID_ADMIN_JSON, UserWebRequest.class);
    verify(validator).validate(STUDENT);
    verify(validator).validate(VALID_ADMIN);
  }
  
}
