package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.model.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import java.util.concurrent.TimeUnit;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {
  
  private static final String COOKIE_NAME = "cookie-name";
  
  private static final String SESSION_ID = "session-id";
  
  private static final String USER_ID = "user-id";
  
  private static final String EMAIL = "email";
  
  private static final String PASSWORD = "password";
  
  private static final Session SESSION = new Session(
    SESSION_ID, USER_ID, null, EMAIL, Role.MENTOR);
  
  private static final User USER = User.builder()
    .id("id")
    .role(Role.MENTOR)
    .email(EMAIL)
    .password(PASSWORD)
    .name("name")
    .phone("phone")
    .address("address")
    .pictureV2(FileV2.builder()
                 .thumbnailUrl("thumbnail-url")
                 .build())
    .build();
  
  private static RedisTemplate<String, Session> redisTemplate;
  
  private static ValueOperations<String, Session> valueOperations;
  
  @Mock
  private UserService userService;
  
  @Mock
  private SessionProperties sessionProperties;
  
  @InjectMocks
  private AuthServiceImpl authService;
  
  @BeforeClass
  public static void setUpClass() {
    
    redisTemplate = mock(RedisTemplate.class);
    valueOperations = mock(ValueOperations.class);
    
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }
  
  @AfterClass
  public static void tearDownClass() {
    
    int numberOfTestMethodInClass = 4;
    
    verify(redisTemplate, times(numberOfTestMethodInClass)).opsForValue();
    
    verify(valueOperations, times(3)).get(SESSION_ID);
    
    verifyNoMoreInteractions(/*redisTemplate,*/valueOperations);
  }
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(userService, sessionProperties);
  }
  
  @Test
  public void testGivenEmailAndPasswordAndHttpServletResponseByLoggingInReturnUser() {
    
    when(userService.getUserByEmailAndPassword(EMAIL, PASSWORD)).thenReturn(
      USER);
    doNothing().when(valueOperations)
      .set(anyString(), any(Session.class));
    when(sessionProperties.getExpireTime()).thenReturn(0);
    when(redisTemplate.expire(anyString(), eq(0),
                              eq(TimeUnit.SECONDS)
    )).thenReturn(true);
    when(sessionProperties.getMaxAge()).thenReturn(0);
    when(sessionProperties.getCookieName()).thenReturn(COOKIE_NAME);
    
    MockHttpServletResponse response = new MockHttpServletResponse();
    
    User retrievedUser = authService.login(EMAIL, PASSWORD, response);
    
    assertThat(retrievedUser).isNotNull();
    assertThat(retrievedUser).isEqualTo(USER);
    
    Cookie cookie = response.getCookie(COOKIE_NAME);
    assertThat(cookie).isNotNull();
    assertThat(cookie.getValue()).isNotBlank();
    
    verify(valueOperations).set(anyString(), any(Session.class));
    //    verify(redisTemplate).expire(anyString(), eq(0), eq(TimeUnit
    // .SECONDS));
    verify(userService).getUserByEmailAndPassword(EMAIL, PASSWORD);
    verify(sessionProperties).getExpireTime();
    verify(sessionProperties).getMaxAge();
    verify(sessionProperties).getCookieName();
  }
  
  @Test
  public void testGivenSessionIdAndHttpServletResponseByLoggingOutReturnSuccessfulOperation() {
    
    when(valueOperations.get(SESSION_ID)).thenReturn(SESSION);
    when(sessionProperties.getCookieName()).thenReturn(COOKIE_NAME);
    
    MockHttpServletResponse response = new MockHttpServletResponse();
    
    authService.logout(SESSION_ID, response);
    
    assertThat(response.getCookie(COOKIE_NAME)
                 .getValue()).isNull();
    
    verify(redisTemplate).delete(SESSION_ID);
    verify(sessionProperties).getCookieName();
  }
  
  @Test
  public void testGivenSessionIdByGettingLoginStatusReturnUser() {
    
    when(valueOperations.get(SESSION_ID)).thenReturn(SESSION);
    when(userService.getUserByEmail(EMAIL)).thenReturn(USER);
    
    User retrievedUser = authService.getLoginStatus(SESSION_ID);
    
    assertThat(retrievedUser).isNotNull();
    assertThat(retrievedUser).isEqualTo(USER);
    
    verify(userService).getUserByEmail(EMAIL);
    verifyZeroInteractions(sessionProperties);
  }
  
  @Test
  public void testGivenInvalidSessionIdByGettingLoginStatusReturnUnauthorizedException() {
    
    when(valueOperations.get(SESSION_ID)).thenReturn(null);
    
    catchException(() -> authService.getLoginStatus(SESSION_ID));
    
    assertThat(caughtException().getClass()).isEqualTo(
      UnauthorizedException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Invalid Session From Service");
    
    verifyZeroInteractions(userService, sessionProperties);
  }
  
}
