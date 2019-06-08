package com.future.function.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.core.Role;
import com.future.function.session.model.Session;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.web.servlet.MockMvc;
import redis.embedded.RedisServer;

import javax.servlet.http.Cookie;
import java.util.Arrays;

public abstract class TestHelper {
  
  protected static final String STUDENT_EMAIL = "student@student.com";
  
  protected static final String MENTOR_EMAIL = "mentor@mentor.com";
  
  protected static final String JUDGE_EMAIL = "judge@judge.com";
  
  protected static final String ADMIN_EMAIL = "admin@admin.com";
  
  protected static final String STUDENT_SESSION_ID = "session-id-student";
  
  protected static final String MENTOR_SESSION_ID = "session-id-mentor";
  
  protected static final String JUDGE_SESSION_ID = "session-id-judge";
  
  protected static final String ADMIN_SESSION_ID = "session-id-admin";
  
  private static final Session STUDENT_SESSION = new Session(
    STUDENT_SESSION_ID, STUDENT_EMAIL, Role.STUDENT);
  
  private static final Session MENTOR_SESSION = new Session(
    MENTOR_SESSION_ID, MENTOR_EMAIL, Role.MENTOR);
  
  private static final Session JUDGE_SESSION = new Session(
    JUDGE_SESSION_ID, JUDGE_EMAIL, Role.JUDGE);
  
  private static final Session ADMIN_SESSION = new Session(
    ADMIN_SESSION_ID, ADMIN_EMAIL, Role.ADMIN);
  
  protected Cookie[] cookies;
  
  protected JacksonTester<BaseResponse> baseResponseJacksonTester;
  
  protected JacksonTester<DataResponse> dataResponseJacksonTester;
  
  protected JacksonTester<PagingResponse> pagingResponseJacksonTester;
  
  @Autowired
  protected MockMvc mockMvc;
  
  private RedisServer redisServer;
  
  private ValueOperations<String, Session> valueOperations;
  
  @Autowired
  private RedisTemplate<String, Session> redisTemplate;
  
  @Before
  protected void setUp() throws Exception {
    
    JacksonTester.initFields(this, new ObjectMapper());
    
    redisServer = new RedisServer(6379);
    redisServer.start();
    
    valueOperations = redisTemplate.opsForValue();
  }
  
  protected void setCookie(Role role) {
    
    Cookie cookie = new Cookie("Function-Session", "");
    
    switch (role) {
      case STUDENT:
        cookie.setValue(STUDENT_SESSION_ID);
        valueOperations.set(STUDENT_SESSION_ID, STUDENT_SESSION);
        break;
      case MENTOR:
        cookie.setValue(MENTOR_SESSION_ID);
        valueOperations.set(MENTOR_SESSION_ID, MENTOR_SESSION);
        break;
      case JUDGE:
        cookie.setValue(JUDGE_SESSION_ID);
        valueOperations.set(JUDGE_SESSION_ID, JUDGE_SESSION);
        break;
      case ADMIN:
        cookie.setValue(ADMIN_SESSION_ID);
        valueOperations.set(ADMIN_SESSION_ID, ADMIN_SESSION);
        break;
      default:
        return;
    }
    
    this.cookies = new Cookie[] { cookie };
  }
  
  @After
  protected void tearDown() {
    
    redisTemplate.delete(
      Arrays.asList(STUDENT_SESSION_ID, MENTOR_SESSION_ID, JUDGE_SESSION_ID,
                    ADMIN_SESSION_ID
      ));
    redisServer.stop();
  }
  
}
