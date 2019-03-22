package com.future.function.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.future.function.web.TestApplication;

/**
 * Use {@code @AutoConfigureMockMvc} to auto configure the MVC mock.
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class InitialControllerTest {

  private static final String RETURNED_STRING = "Hello";

  @Autowired
  private MockMvc mockMvc;

  /**
   * Define setUp before any test, if none required keep it empty.
   */
  @Before
  public void setUp() throws Exception {

  }

  /**
   * Define tearDown after any test, always empty the database.
   */
  @After
  public void tearDown() throws Exception {

  }

  /**
   * Use the following test naming convention:
   * {@code public void testGiven(Condition)By(ExpectedBehavior)Return(Result)}
   * <p>
   * Use Assertions instead of JUnit.
   */
  @Test
  public void testGivenGetMethodCallToInitialControllerByReturningObjectOnGetCallReturnStringHello() throws Exception {

    MockHttpServletRequestBuilder requestBuilder = get("/api/test");

    MockHttpServletResponse response = mockMvc.perform(requestBuilder)
        .andReturn()
        .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo(RETURNED_STRING);
  }
}
