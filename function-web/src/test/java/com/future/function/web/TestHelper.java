package com.future.function.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.core.Role;
import com.future.function.session.model.Session;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

public abstract class TestHelper {
  
  protected static final String STUDENT_EMAIL = "student@student.com";
  
  protected static final String MENTOR_EMAIL = "mentor@mentor.com";
  
  protected static final String JUDGE_EMAIL = "judge@judge.com";
  
  protected static final String ADMIN_EMAIL = "admin@admin.com";
  
  protected static final String STUDENT_SESSION_ID = "session-id-student";
  
  protected static final String MENTOR_SESSION_ID = "session-id-mentor";
  
  protected static final String JUDGE_SESSION_ID = "session-id-judge";
  
  protected static final String ADMIN_SESSION_ID = "session-id-admin";
  
  protected static final Session STUDENT_SESSION = new Session(
    STUDENT_SESSION_ID, STUDENT_EMAIL, Role.STUDENT);
  
  protected static final Session MENTOR_SESSION = new Session(
    MENTOR_SESSION_ID, MENTOR_EMAIL, Role.MENTOR);
  
  protected static final Session JUDGE_SESSION = new Session(
    JUDGE_SESSION_ID, JUDGE_EMAIL, Role.JUDGE);
  
  protected static final Session ADMIN_SESSION = new Session(
    ADMIN_SESSION_ID, ADMIN_EMAIL, Role.ADMIN);
  
  protected Cookie[] cookies;
  
  protected JacksonTester<BaseResponse> baseResponseJacksonTester;
  
  protected JacksonTester<DataResponse> dataResponseJacksonTester;
  
  protected JacksonTester<PagingResponse> pagingResponseJacksonTester;
  
  @Autowired
  protected MockMvc mockMvc;
  
  @Before
  protected void setUp() {
    
    JacksonTester.initFields(this, new ObjectMapper());
  }
  
  protected void setCookie(Role role) {
    
    Cookie cookie = new Cookie("Function-Session", "");
    
    switch (role) {
      case STUDENT:
        cookie.setValue(STUDENT_SESSION_ID);
        break;
      case MENTOR:
        cookie.setValue(MENTOR_SESSION_ID);
        break;
      case JUDGE:
        cookie.setValue(JUDGE_SESSION_ID);
        break;
      case ADMIN:
        cookie.setValue(ADMIN_SESSION_ID);
        break;
      default:
        return;
    }
    
    this.cookies = new Cookie[] { cookie };
  }
  
}
