package com.future.function.session.resolver;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionResolverTest {
  
  private static final String COOKIE_NAME = "cookie-name";
  
  private static final String COOKIE_VALUE = "cookie-value";
  
  private static final Session SESSION = new Session(
    COOKIE_VALUE, "", Role.MENTOR);
  
  private static RedisTemplate<String, Session> redisTemplate;
  
  private static ValueOperations<String, Session> valueOperations;
  
  @Mock
  private SessionProperties sessionProperties;
  
  @Mock
  private HttpServletRequest servletRequest;
  
  private MethodParameter sessionParameterWithoutRole;
  
  private MethodParameter sessionParameterWithRole;
  
  private MethodParameter nonInjectedParameter;
  
  private MethodParameter nonAnnotatedParameter;
  private MethodParameter incorrectlyAnnotatedParameter;
  
  private SessionResolver sessionResolver;
  
  private NativeWebRequest webRequest;
  
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
    
    verify(valueOperations, times(3)).get(COOKIE_VALUE);
    
    verifyNoMoreInteractions(redisTemplate, valueOperations);
  }
  
  @Before
  public void setUp() {
    
    sessionResolver = new SessionResolver(redisTemplate, sessionProperties);
    webRequest = new ServletWebRequest(servletRequest);
    
    Method methodWithoutRole = ReflectionUtils.findMethod(
      this.getClass(), "sessionInjectedMethodWithoutRole", (Class<?>[]) null);
    sessionParameterWithoutRole = new SynthesizingMethodParameter(
      methodWithoutRole, 0);
    
    Method methodWithRole = ReflectionUtils.findMethod(
      this.getClass(), "sessionInjectedMethodWithRole", (Class<?>[]) null);
    sessionParameterWithRole = new SynthesizingMethodParameter(
      methodWithRole, 0);
    
    Method nonInjectedMethod = ReflectionUtils.findMethod(
      this.getClass(), "nonInjectedMethod", (Class<?>[]) null);
    nonInjectedParameter = new SynthesizingMethodParameter(
      nonInjectedMethod, 0);
    
    Method nonAnnotatedMethod = ReflectionUtils.findMethod(
      this.getClass(), "nonAnnotatedMethod", (Class<?>[]) null);
    nonAnnotatedParameter = new SynthesizingMethodParameter(
      nonAnnotatedMethod, 0);
    
    Method incorrectlyAnnotatedMethod = ReflectionUtils.findMethod(
      this.getClass(), "incorrectlyAnnotatedMethod", (Class<?>[]) null);
    incorrectlyAnnotatedParameter = new SynthesizingMethodParameter(
      incorrectlyAnnotatedMethod, 0);
    
    when(sessionProperties.getCookieName()).thenReturn(COOKIE_NAME);
    
    when(servletRequest.getCookies()).thenReturn(new Cookie[] {
      new Cookie(COOKIE_NAME, COOKIE_VALUE)
    });
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(sessionProperties, servletRequest);
  }
  
  @Test
  public void testGivenParameterByCheckingIfParameterIsSupportedReturnBoolean() {
    
    assertThat(
      sessionResolver.supportsParameter(sessionParameterWithoutRole)).isTrue();
    assertThat(
      sessionResolver.supportsParameter(sessionParameterWithRole)).isTrue();
    assertThat(
      sessionResolver.supportsParameter(nonInjectedParameter)).isFalse();
    assertThat(
      sessionResolver.supportsParameter(nonAnnotatedParameter)).isFalse();
    assertThat(
      sessionResolver.supportsParameter(incorrectlyAnnotatedParameter)).isFalse();
  }
  
  @Test
  public void testGivenParameterWithRoleByInjectingValueToParameterReturnInjectedParameter()
    throws Exception {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(SESSION);
    
    assertThat(sessionResolver.resolveArgument(sessionParameterWithRole, null,
                                               webRequest, null
    )).isNotNull();
    
    verify(servletRequest).getCookies();
    verify(sessionProperties).getCookieName();
  }
  
  @Test
  public void testGivenParameterWithRoleAndInvalidCookieByInjectingValueToParameterReturnUnauthorizedException() {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(null);
    
    catchException(
      () -> sessionResolver.resolveArgument(sessionParameterWithRole, null,
                                            webRequest, null
      ));
    
    assertThat(caughtException().getClass()).isEqualTo(
      UnauthorizedException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Invalid Session From Resolver");
    
    verify(servletRequest).getCookies();
    verify(sessionProperties).getCookieName();
  }
  
  @Test
  public void testGivenParameterWithoutRoleByInjectingValueToParameterReturnInjectedParameter()
    throws Exception {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(SESSION);
    
    assertThat(
      sessionResolver.resolveArgument(sessionParameterWithoutRole, null,
                                      webRequest, null
      )).isNotNull();
    
    verify(servletRequest).getCookies();
    verify(sessionProperties).getCookieName();
  }
  
  @SuppressWarnings("unused")
  private void sessionInjectedMethodWithRole(
    @WithAnyRole(roles = Role.MENTOR)
      Session session
  ) {}
  
  @SuppressWarnings("unused")
  private void sessionInjectedMethodWithoutRole(
    @WithAnyRole
      Session session
  ) {}
  
  @SuppressWarnings("unused")
  private void nonInjectedMethod(String string) {}
  
  @SuppressWarnings("unused")
  private void nonAnnotatedMethod(
    Session session
  ) {}
  
  @SuppressWarnings("unused")
  private void incorrectlyAnnotatedMethod(
    @WithAnyRole
      String string
  ) {}
  
}
