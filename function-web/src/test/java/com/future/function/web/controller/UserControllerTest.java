package com.future.function.web.controller;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.user.User;
import com.future.function.model.util.constant.Role;
import com.future.function.service.api.feature.user.UserService;
import com.future.function.web.mapper.request.UserRequestMapper;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
  
  private static final String ADDRESS = "address";
  
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
  
  private static final Pageable PAGEABLE = new PageRequest(0, 10);
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private UserService userService;
  
  @MockBean
  private UserRequestMapper userRequestMapper;
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(userService);
    verifyNoMoreInteractions(userRequestMapper);
  }
  
  @Before
  public void setUp() {}
  
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
    
    verify(userService, times(1)).getUsers(Role.STUDENT, PAGEABLE);
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
    
    verify(userService, times(1)).deleteUser(STUDENT_EMAIL);
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
    
    verify(userService, times(1)).getUser(STUDENT_EMAIL);
    verifyZeroInteractions(userRequestMapper);
  }
  
}
