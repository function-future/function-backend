package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.mapper.request.core.UserRequestMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

  private static final String ADDRESS = "address";

  private static final String NAME = "name";

  private static final Long NUMBER = 1L;

  private static final String PHONE = "081212341234";

  private static final String STUDENT_EMAIL = "student@test.com";

  private static final String UNIVERSITY = "university";

  private static final String PICTURE_URL = "picture-url";

  private static final File PICTURE = File.builder()
          .fileUrl(PICTURE_URL)
          .build();

  private static final User STUDENT = User.builder()
          .role(Role.STUDENT)
          .email(STUDENT_EMAIL)
          .name(NAME)
          .phone(PHONE)
          .address(ADDRESS)
          .picture(PICTURE)
          .batch(Batch.builder()
                  .number(NUMBER)
                  .build())
          .university(UNIVERSITY)
          .build();

  private static final String STUDENT_JSON =
          "{\n" + "    \"role\": \"STUDENT\",\n" + "    \"email\": \"" +
                  STUDENT_EMAIL + "\",\n" + "    \"name\": \"" + NAME + "\",\n" +
                  "    \"phone\": \"" + PHONE + "\",\n" + "    \"address\": \"" + ADDRESS +
                  "\",\n" + "    \"batch\": " + NUMBER + ",\n" + "    \"university\": \"" +
                  UNIVERSITY + "\"\n" + "}";

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRequestMapper userRequestMapper;

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(userService, userRequestMapper);
  }

  @Test
  public void testGivenCallToUsersApiByGettingUsersFromUserServiceReturnPagingResponseOfUsers()
          throws Exception {

    List<User> studentsList = Arrays.asList(STUDENT, STUDENT, STUDENT);

    given(userService.getUsers(Role.STUDENT, PAGEABLE)).willReturn(
            new PageImpl<>(studentsList, PAGEABLE, studentsList.size()));

    MockHttpServletResponse response = mockMvc.perform(
            get("/api/core/users").param("role", Role.STUDENT.name()))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(userService).getUsers(Role.STUDENT, PAGEABLE);
    verifyZeroInteractions(userRequestMapper);
  }

  @Test
  public void testGivenEmailFromPathVariableByDeletingUserByEmailReturnBaseResponseOK()
          throws Exception {

    MockHttpServletResponse response = mockMvc.perform(
            delete("/api/core/users/" + STUDENT_EMAIL))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(userService).deleteUser(STUDENT_EMAIL);
    verifyZeroInteractions(userRequestMapper);
  }

  @Test
  public void testGivenEmailFromPathVariableByGettingUserByEmailReturnDataResponseUser()
          throws Exception {

    given(userService.getUser(STUDENT_EMAIL)).willReturn(STUDENT);

    MockHttpServletResponse response = mockMvc.perform(
            get("/api/core/users/" + STUDENT_EMAIL))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(userService).getUser(STUDENT_EMAIL);
    verifyZeroInteractions(userRequestMapper);
  }

  @Test
  public void testGivenUserDataAsStringAndImageByCreatingUserReturnDataResponseUser()
          throws Exception {

    User student = User.builder()
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

    given(userRequestMapper.toUser(STUDENT_JSON)).willReturn(student);
    given(userService.createUser(student, null)).willReturn(STUDENT);

    MockHttpServletResponse response = mockMvc.perform(post(
            "/api/core/users").contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .param("data",
                    STUDENT_JSON
            )
            .param("image", ""))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(userService).createUser(student, null);
    verify(userRequestMapper).toUser(STUDENT_JSON);
  }

  @Test
  public void testGivenEmailFromPathVariableAndUserDataAsStringaAndImageByUpdatingUserReturnDataResponseUser()
          throws Exception {

    User student = User.builder()
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

    given(userRequestMapper.toUser(STUDENT_EMAIL, STUDENT_JSON)).willReturn(
            student);
    given(userService.updateUser(student, null)).willReturn(STUDENT);

    MockHttpServletResponse response = mockMvc.perform(put(
            "/api/core/users/" + STUDENT_EMAIL).contentType(
            MediaType.MULTIPART_FORM_DATA_VALUE)
            .param("data",
                    STUDENT_JSON
            )
            .param("image", ""))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(userService).updateUser(student, null);
    verify(userRequestMapper).toUser(STUDENT_EMAIL, STUDENT_JSON);
  }

}
