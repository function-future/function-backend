package com.future.function.web.controller;

import com.future.function.web.dummy.controller.BadController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ExceptionController.class, BadController.class})
public class ExceptionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void givenBadRequestExceptionRaisedByExceptionHandlingReturnErrorResponse()
          throws Exception {

    mockMvc.perform(get("/bad-request"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json(
                    "{\"code\":400,\"status\":\"BAD_REQUEST\", \"errors\":{}}"));
    mockMvc.perform(get("/bad-request-set"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json(
                    "{\"code\":400,\"status\":\"BAD_REQUEST\", \"errors\":{}}"));
  }

  @Test
  public void givenUnauthorizedExceptionRaisedByExceptionHandlingReturnErrorResponse()
          throws Exception {

    mockMvc.perform(get("/unauthorized"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{\"code\":401,\"status\":\"UNAUTHORIZED\"}"));
    mockMvc.perform(get("/unauthorized-throwable"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{\"code\":401,\"status\":\"UNAUTHORIZED\"}"));
  }

  @Test
  public void givenForbiddenExceptionRaisedByExceptionHandlingReturnErrorResponse()
          throws Exception {

    mockMvc.perform(get("/forbidden"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{\"code\":403,\"status\":\"FORBIDDEN\"}"));
    mockMvc.perform(get("/forbidden-throwable"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{\"code\":403,\"status\":\"FORBIDDEN\"}"));
  }

  @Test
  public void givenNotFoundExceptionRaisedByExceptionHandlingReturnErrorResponse()
          throws Exception {

    mockMvc.perform(get("/not-found"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{\"code\":404,\"status\":\"NOT_FOUND\"}"));
    mockMvc.perform(get("/not-found-throwable"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().json("{\"code\":404,\"status\":\"NOT_FOUND\"}"));
  }

}
