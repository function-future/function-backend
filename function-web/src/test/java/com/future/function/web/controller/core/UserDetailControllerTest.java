package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.AccessService;
import com.future.function.service.api.feature.core.MenuService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.UserDetailRequestMapper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = UserDetailController.class)
public class UserDetailControllerTest extends TestHelper {

  private static final User USER = User.builder()
    .id("id")
    .role(Role.MENTOR)
    .email("email")
    .name("name")
    .phone("phone")
    .address("address")
    .pictureV2(FileV2.builder()
                 .fileUrl("file-url")
                 .build())
    .build();

  private static final String OLD_PASSWORD = "old-password";

  private static final String NEW_PASSWORD = "new-password";

  private static final ChangePasswordWebRequest CHANGE_PASSWORD_WEB_REQUEST =
    new ChangePasswordWebRequest(OLD_PASSWORD, NEW_PASSWORD);

  private static final DataResponse<UserWebResponse> DATA_RESPONSE =
    UserResponseMapper.toUserDataResponse(USER);

  private static final BaseResponse UNAUTHORIZED_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.UNAUTHORIZED);

  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);

  private JacksonTester<ChangePasswordWebRequest>
    changePasswordWebRequestJacksonTester;

  private JacksonTester<Map<String, Object>> mapJacksonTester;

  @MockBean
  private UserService userService;

  @MockBean
  private MenuService menuService;

  @MockBean
  private AccessService accessService;

  @MockBean
  private UserDetailRequestMapper userDetailRequestMapper;

  @Override
  @Before
  public void setUp() {

    super.setUp();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      userService, menuService, accessService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallAndValidRoleByGettingProfileReturnDataResponse()
    throws Exception {

    super.setCookie(Role.MENTOR);

    when(userService.getUserByEmail(MENTOR_EMAIL)).thenReturn(USER);

    mockMvc.perform(get("/api/core/user/profile").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));

    verify(userService).getUserByEmail(MENTOR_EMAIL);
    verifyZeroInteractions(accessService, menuService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallAndInvalidSessionByGettingProfileReturnUnauthorizedBaseResponse()
    throws Exception {

    mockMvc.perform(get("/api/core/user/profile"))
      .andExpect(status().isUnauthorized())
      .andExpect(content().json(
        baseResponseJacksonTester.write(UNAUTHORIZED_BASE_RESPONSE)
          .getJson()));

    verifyZeroInteractions(
      userService, accessService, menuService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallByChangingPasswordReturnBaseResponse()
    throws Exception {

    super.setCookie(Role.MENTOR);

    when(userDetailRequestMapper.toOldAndNewPasswordPair(
      CHANGE_PASSWORD_WEB_REQUEST)).thenReturn(
      Pair.of(OLD_PASSWORD, NEW_PASSWORD));

    mockMvc.perform(post("/api/core/user/password").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(changePasswordWebRequestJacksonTester.write(
                        CHANGE_PASSWORD_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));

    verify(userDetailRequestMapper).toOldAndNewPasswordPair(
      CHANGE_PASSWORD_WEB_REQUEST);
    verify(userService).changeUserPassword(
      MENTOR_EMAIL, OLD_PASSWORD, NEW_PASSWORD);
    verifyZeroInteractions(accessService, menuService);
  }

  @Test
  public void testGivenApiCallAndLoggedInUserByGettingMenuListReturnMap()
    throws Exception {

    super.setCookie(Role.JUDGE);

    Map<String, Object> menuList = Collections.singletonMap("key", true);
    when(menuService.getSectionsByRole(Role.JUDGE)).thenReturn(menuList);

    mockMvc.perform(get("/api/core/user/menu-list").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(mapJacksonTester.write(menuList)
                                  .getJson()));

    verify(menuService).getSectionsByRole(Role.JUDGE);
    verifyZeroInteractions(userService, accessService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallAndNotLoggedInUserByGettingMenuListReturnDefaultMap()
    throws Exception {

    Map<String, Object> defaultMenuList = Collections.emptyMap();
    when(menuService.getSectionsByRole(null)).thenReturn(defaultMenuList);

    mockMvc.perform(get("/api/core/user/menu-list"))
      .andExpect(status().isOk())
      .andExpect(content().json(mapJacksonTester.write(defaultMenuList)
                                  .getJson()));

    verify(menuService).getSectionsByRole(Role.UNKNOWN);
    verifyZeroInteractions(userService, accessService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallAndUrlAndLoggedInUserByGettingAccessListReturnMap()
    throws Exception {

    super.setCookie(Role.JUDGE);

    String url = "url";
    Map<String, Object> accessList = Collections.singletonMap("key", true);
    when(accessService.getComponentsByUrlAndRole(url, Role.JUDGE)).thenReturn(
      accessList);

    mockMvc.perform(get("/api/core/user/access-list").cookie(cookies)
                      .param("url", url))
      .andExpect(status().isOk())
      .andExpect(content().json(mapJacksonTester.write(accessList)
                                  .getJson()));

    verify(accessService).getComponentsByUrlAndRole(url, Role.JUDGE);
    verifyZeroInteractions(userService, menuService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallAndUrlAndNotLoggedInUserByGettingAccessListReturnDefaultMap()
    throws Exception {

    String url = "url";
    Map<String, Object> defaultAccessList = Collections.emptyMap();
    when(accessService.getComponentsByUrlAndRole(url, null)).thenReturn(
      defaultAccessList);

    mockMvc.perform(get("/api/core/user/access-list").param("url", url))
      .andExpect(status().isOk())
      .andExpect(content().json(mapJacksonTester.write(defaultAccessList)
                                  .getJson()));

    verify(accessService).getComponentsByUrlAndRole(url, Role.UNKNOWN);
    verifyZeroInteractions(userService, menuService, userDetailRequestMapper);
  }

  @Test
  public void testGivenApiCallAndNotLoggedInUserByGettingAccessListReturnDefaultMap()
    throws Exception {

    Map<String, Object> defaultAccessList = Collections.emptyMap();
    when(accessService.getComponentsByUrlAndRole("", null)).thenReturn(
      defaultAccessList);

    mockMvc.perform(get("/api/core/user/access-list"))
      .andExpect(status().isOk())
      .andExpect(content().json(mapJacksonTester.write(defaultAccessList)
                                  .getJson()));

    verify(accessService).getComponentsByUrlAndRole("", Role.UNKNOWN);
    verifyZeroInteractions(userService, menuService, userDetailRequestMapper);
  }

}
