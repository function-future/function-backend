package com.future.function.session.resolver;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
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
    COOKIE_VALUE, "", "", "", Role.MENTOR);
  
  private static RedisTemplate<String, Session> redisTemplate;
  
  private static ValueOperations<String, Session> valueOperations;
  
  @Mock
  private SessionProperties sessionProperties;
  
  @Mock
  private HttpServletRequest servletRequest;
  
  private MethodParameter sessionParameterWithoutRole;
  
  private MethodParameter sessionParameterWithRole;
  
  private MethodParameter classLevelAnnotatedMethod;
  
  private MethodParameter methodLevelAnnotatedMethod;
  
  private MethodParameter nonAnnotatedParameter;
  
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
    
    int numberOfTestMethodInClass = 8;
    
    verify(redisTemplate, times(numberOfTestMethodInClass)).opsForValue();
    
    verify(valueOperations, times(7)).get(COOKIE_VALUE);
    
    verifyNoMoreInteractions(redisTemplate, valueOperations);
  }
  
  @Before
  public void setUp() {
    
    sessionResolver = new SessionResolver(redisTemplate, sessionProperties);
    webRequest = new ServletWebRequest(servletRequest);
    
    Method methodWithClassLevelAnnotation = ReflectionUtils.findMethod(
      ClassLevelAnnotatedClass.class, "methodOfClassLevelAnnotatedClass",
      (Class<?>[]) null
    );
    classLevelAnnotatedMethod = new SynthesizingMethodParameter(
      methodWithClassLevelAnnotation, 0);
    
    Method methodWithMethodLevelAnnotation = ReflectionUtils.findMethod(
      this.getClass(), "methodOfMethodLevelAnnotated", (Class<?>[]) null);
    methodLevelAnnotatedMethod = new SynthesizingMethodParameter(
      methodWithMethodLevelAnnotation, 0);
    
    Method methodWithoutRole = ReflectionUtils.findMethod(
      this.getClass(), "sessionInjectedMethodWithoutRole", (Class<?>[]) null);
    sessionParameterWithoutRole = new SynthesizingMethodParameter(
      methodWithoutRole, 0);
    
    Method methodWithRole = ReflectionUtils.findMethod(
      this.getClass(), "sessionInjectedMethodWithRole", (Class<?>[]) null);
    sessionParameterWithRole = new SynthesizingMethodParameter(
      methodWithRole, 0);
    
    Method nonAnnotatedMethod = ReflectionUtils.findMethod(
      this.getClass(), "nonAnnotatedMethod", (Class<?>[]) null);
    nonAnnotatedParameter = new SynthesizingMethodParameter(
      nonAnnotatedMethod, 0);
    
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
      sessionResolver.supportsParameter(classLevelAnnotatedMethod)).isTrue();
    assertThat(
      sessionResolver.supportsParameter(methodLevelAnnotatedMethod)).isTrue();
    assertThat(
      sessionResolver.supportsParameter(sessionParameterWithoutRole)).isTrue();
    assertThat(
      sessionResolver.supportsParameter(sessionParameterWithRole)).isTrue();
    assertThat(
      sessionResolver.supportsParameter(nonAnnotatedParameter)).isFalse();
  }
  
  @Test
  public void testGivenAnnotatedClassByInjectingValueToParameterReturnInjectedParameter()
    throws Exception {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(SESSION);
    
    assertThat(sessionResolver.resolveArgument(classLevelAnnotatedMethod, null,
                                               webRequest, null
    )).isNotNull();
    
    verify(servletRequest).getCookies();
    verify(sessionProperties).getCookieName();
  }
  
  @Test
  public void testGivenAnnotatedMethodByInjectingValueToParameterReturnInjectedParameter()
    throws Exception {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(SESSION);
    
    assertThat(sessionResolver.resolveArgument(methodLevelAnnotatedMethod, null,
                                               webRequest, null
    )).isNotNull();
    
    verify(servletRequest).getCookies();
    verify(sessionProperties).getCookieName();
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
  
  @Test
  public void testGivenParameterWithoutRoleAndNoUnauthorizedByInjectingValueToParameterReturnInjectedParameter()
    throws Exception {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(null);
    
    assertThat(
      sessionResolver.resolveArgument(sessionParameterWithoutRole, null,
                                      webRequest, null
      )).isNotNull();
    
    verify(servletRequest).getCookies();
    verify(sessionProperties).getCookieName();
  }
  
  @Test
  public void testGivenNonAnnotatedMethodByInjectingValueToParameterReturnUnauthorizedException() {
    
    when(valueOperations.get(COOKIE_VALUE)).thenReturn(SESSION);
    
    catchException(
      () -> sessionResolver.resolveArgument(nonAnnotatedParameter, null,
                                            webRequest, null
      ));
    
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid Role");
    
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
    @WithAnyRole(noUnauthorized = true)
      Session session
  ) {}
  
  @SuppressWarnings("unused")
  private void nonAnnotatedMethod(
    String string
  ) {}
  
  @WithAnyRole
  @SuppressWarnings("unused")
  private void methodOfMethodLevelAnnotated() {}
  
  @WithAnyRole
  class ClassLevelAnnotatedClass {
    
    @SuppressWarnings("unused")
    public void methodOfClassLevelAnnotatedClass() {}
    
  }
  
}
