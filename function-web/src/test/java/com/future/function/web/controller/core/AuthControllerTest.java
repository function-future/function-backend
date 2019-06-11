package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.AuthService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.AuthResponseMapper;
import com.future.function.web.model.request.core.AuthWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.AuthWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = AuthController.class)
public class AuthControllerTest extends TestHelper {
  
  private static final User USER = User.builder()
    .id("id")
    .role(Role.MENTOR)
    .email(MENTOR_EMAIL)
    .name("name")
    .phone("phone")
    .address("address")
    .pictureV2(FileV2.builder()
                 .thumbnailUrl("thumbnail-url")
                 .build())
    .build();
  
  private static final String PASSWORD = "password";
  
  private static final AuthWebRequest AUTH_WEB_REQUEST = new AuthWebRequest(
    MENTOR_EMAIL, PASSWORD);
  
  private static final DataResponse<AuthWebResponse> DATA_RESPONSE =
    AuthResponseMapper.toAuthDataResponse(USER);
  
  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);
  
  private JacksonTester<AuthWebRequest> authWebRequestJacksonTester;
  
  @MockBean
  private AuthService authService;
  
  @Override
  @Before
  public void setUp() {
    
    super.setUp();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(authService);
  }
  
  @Test
  public void testGivenApiCallByLoggingUserInReturnDataResponse()
    throws Exception {
    
    when(authService.login(eq(MENTOR_EMAIL), eq(PASSWORD),
                           any(HttpServletResponse.class)
    )).thenReturn(USER);
    
    mockMvc.perform(post("/api/core/auth").contentType(
      MediaType.APPLICATION_JSON)
                      .content(
                        authWebRequestJacksonTester.write(AUTH_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));
    
    verify(authService).login(
      eq(MENTOR_EMAIL), eq(PASSWORD), any(HttpServletResponse.class));
  }
  
  @Test
  public void testGivenApiCallAndCookieByGettingLoginStatusReturnDataResponse()
    throws Exception {
    
    super.setCookie(Role.MENTOR);
    
    when(authService.getLoginStatus(MENTOR_SESSION_ID)).thenReturn(USER);
    
    mockMvc.perform(get("/api/core/auth").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));
    
    verify(authService).getLoginStatus(MENTOR_SESSION_ID);
  }
  
  @Test
  public void testGivenApiCallAndCookieByLoggingUserOutReturnBaseResponse()
    throws Exception {
    
    super.setCookie(Role.MENTOR);
    
    mockMvc.perform(delete("/api/core/auth").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));
    
    verify(authService).logout(
      eq(MENTOR_SESSION_ID), any(HttpServletResponse.class));
  }
  
}
