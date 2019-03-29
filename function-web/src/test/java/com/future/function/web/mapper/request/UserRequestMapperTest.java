package com.future.function.web.mapper.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.user.User;
import com.future.function.model.util.constant.Role;
import com.future.function.web.model.request.user.UserWebRequest;

@RunWith(MockitoJUnitRunner.class)
public class UserRequestMapperTest {

  private static final String ADDRESS = "address";

  private static final String ADMIN_EMAIL = "admin@test.com";

  private static final String BAD_JSON = "{}";

  private static final String NAME = "name";

  private static final Long NUMBER = 1L;

  private static final String PHONE = "081212341234";

  private static final String STUDENT_EMAIL = "student@test.com";

  private static final String UNIVERSITY = "university";

  private static final User STUDENT = User.builder()
      .role(Role.STUDENT)
      .email(STUDENT_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .batch(Batch.builder()
          .number(NUMBER)
          .build())
      .university(UNIVERSITY)
      .build();

  private static final String STUDENT_JSON =
      "{\n" + "    \"role\": \"STUDENT\",\n" + "    \"email\": \"" + ADMIN_EMAIL + "\",\n" + "    \"name\": \"" + NAME +
          "\",\n" + "    \"phone\": \"" + PHONE + "\",\n" + "    \"address\": \"" + ADDRESS + "\",\n" +
          "    \"batch\": " + NUMBER + ",\n" + "    \"university\": \"" + UNIVERSITY + "\"\n" + "}";

  private static final UserWebRequest STUDENT_WEB_REQUEST =
      new UserWebRequest(Role.STUDENT.name(), STUDENT_EMAIL, NAME, PHONE, ADDRESS, NUMBER, UNIVERSITY);

  private static final User INVALID_ADMIN = User.builder()
      .role(Role.ADMIN)
      .email(ADMIN_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .batch(Batch.builder()
          .number(NUMBER)
          .build())
      .university(UNIVERSITY)
      .build();

  private static final String INVALID_ADMIN_JSON =
      "{\n" + "    \"role\": \"ADMIN\",\n" + "    \"email\": \"" + ADMIN_EMAIL + "\",\n" + "    \"name\": \"" + NAME +
          "\",\n" + "    \"phone\": \"" + PHONE + "\",\n" + "    \"address\": \"" + ADDRESS + "\",\n" +
          "    \"batch\": " + NUMBER + ",\n" + "    \"university\": \"" + UNIVERSITY + "\"\n" + "}";

  private static final UserWebRequest INVALID_ADMIN_WEB_REQUEST =
      new UserWebRequest(Role.ADMIN.name(), ADMIN_EMAIL, NAME, PHONE, ADDRESS, NUMBER, UNIVERSITY);

  private static final User VALID_ADMIN = User.builder()
      .role(Role.ADMIN)
      .email(ADMIN_EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .build();

  private static final String VALID_ADMIN_JSON =
      "{\n" + "    \"role\": \"ADMIN\",\n" + "    \"email\": \"" + ADMIN_EMAIL + "\",\n" + "    \"name\": \"" + NAME +
          "\",\n" + "    \"phone\": \"" + PHONE + "\",\n" + "    \"address\": \"" + ADDRESS + "\"\n" + "}";

  private static final UserWebRequest VALID_ADMIN_WEB_REQUEST =
      new UserWebRequest(Role.ADMIN.name(), ADMIN_EMAIL, NAME, PHONE, ADDRESS, null, null);

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private UserRequestMapper userRequestMapper;

  @Mock
  private ObjectValidator validator;

  @Before
  public void setUp() throws Exception {

    when(objectMapper.readValue(STUDENT_JSON, UserWebRequest.class)).thenReturn(STUDENT_WEB_REQUEST);
    when(objectMapper.readValue(INVALID_ADMIN_JSON, UserWebRequest.class)).thenReturn(INVALID_ADMIN_WEB_REQUEST);
    when(objectMapper.readValue(VALID_ADMIN_JSON, UserWebRequest.class)).thenReturn(VALID_ADMIN_WEB_REQUEST);
    when(objectMapper.readValue(BAD_JSON, UserWebRequest.class)).thenThrow(new IOException());
    when(validator.validate(STUDENT)).thenReturn(STUDENT);
    when(validator.validate(VALID_ADMIN)).thenReturn(VALID_ADMIN);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(objectMapper);
    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testGivenJsonDataWithInvalidFormatAsStringByParsingToUserClassReturnRuntimeException() throws Exception {

    try {
      userRequestMapper.toUser(BAD_JSON);
    } catch (Exception e) {
      assertThat(e).isInstanceOf(RuntimeException.class);
      assertThat(e.getMessage()).isEqualTo("Bad Request");
    }

    verify(objectMapper, times(1)).readValue(BAD_JSON, UserWebRequest.class);
  }

  @Test
  public void testGivenJsonDataAsStringByParsingToUserClassReturnUserObject() throws Exception {

    User parsedStudent = userRequestMapper.toUser(STUDENT_JSON);

    assertThat(parsedStudent).isEqualTo(STUDENT);

    User parsedAdmin = userRequestMapper.toUser(VALID_ADMIN_JSON);

    assertThat(parsedAdmin).isEqualTo(VALID_ADMIN);

    verify(objectMapper, times(1)).readValue(STUDENT_JSON, UserWebRequest.class);
    verify(objectMapper, times(1)).readValue(VALID_ADMIN_JSON, UserWebRequest.class);
    verify(validator, times(1)).validate(STUDENT);
    verify(validator, times(1)).validate(VALID_ADMIN);
  }

  @Test
  public void testGivenJsonDataAsStringWithInvalidDataByParsingToUserClassReturnConstraintViolationException() {

  }

}
