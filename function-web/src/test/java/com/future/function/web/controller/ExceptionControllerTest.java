package com.future.function.web.controller;

import com.future.function.web.JacksonTestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.dummy.controller.BadController;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.BaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = { ExceptionController.class, BadController.class })
public class ExceptionControllerTest extends JacksonTestHelper {
  
  private static final BaseResponse BAD_REQUEST_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.BAD_REQUEST);
  
  private static final BaseResponse UNAUTHORIZED_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.UNAUTHORIZED);
  
  private static final BaseResponse FORBIDDEN_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.FORBIDDEN);
  
  private static final BaseResponse NOT_FOUND_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.NOT_FOUND);
  
  private static final BaseResponse INTERNAL_SERVER_ERROR_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.INTERNAL_SERVER_ERROR);
  
  @Autowired
  private MockMvc mockMvc;
  
  @Before
  public void setUp() {
  
    super.setUp();
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void givenBadRequestExceptionRaisedByExceptionHandlingReturnErrorResponse()
    throws Exception {
    
    mockMvc.perform(get("/bad-request"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(BAD_REQUEST_RESPONSE)
          .getJson()));
    mockMvc.perform(get("/bad-request-set"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(BAD_REQUEST_RESPONSE)
          .getJson()));
  }
  
  @Test
  public void givenUnauthorizedExceptionRaisedByExceptionHandlingReturnErrorResponse()
    throws Exception {
    
    mockMvc.perform(get("/unauthorized"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(UNAUTHORIZED_RESPONSE)
          .getJson()));
    mockMvc.perform(get("/unauthorized-throwable"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(UNAUTHORIZED_RESPONSE)
          .getJson()));
  }
  
  @Test
  public void givenForbiddenExceptionRaisedByExceptionHandlingReturnErrorResponse()
    throws Exception {
    
    mockMvc.perform(get("/forbidden"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(FORBIDDEN_RESPONSE)
          .getJson()));
    mockMvc.perform(get("/forbidden-throwable"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(FORBIDDEN_RESPONSE)
          .getJson()));
  }
  
  @Test
  public void givenNotFoundExceptionRaisedByExceptionHandlingReturnErrorResponse()
    throws Exception {
    
    mockMvc.perform(get("/not-found"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(NOT_FOUND_RESPONSE)
          .getJson()));
    mockMvc.perform(get("/not-found-throwable"))
      .andExpect(status().is4xxClientError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(NOT_FOUND_RESPONSE)
          .getJson()));
  }
  
  @Test
  public void givenUnsupportedOperationExceptionRaisedByExceptionHandlingReturnErrorResponse()
    throws Exception {
    
    mockMvc.perform(get("/unsupported-operation"))
      .andExpect(status().isInternalServerError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(INTERNAL_SERVER_ERROR_RESPONSE)
          .getJson()));
    mockMvc.perform(get("/unsupported-operation-throwable"))
      .andExpect(status().isInternalServerError())
      .andExpect(content().json(
        baseResponseJacksonTester.write(INTERNAL_SERVER_ERROR_RESPONSE)
          .getJson()));
  }
  
}
