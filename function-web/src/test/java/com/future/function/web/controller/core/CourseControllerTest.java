package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = CourseController.class)
public class CourseControllerTest extends TestHelper {

  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);

  private static final String ID = "id";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final Course COURSE = Course.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .build();

  private static final CourseWebRequest COURSE_WEB_REQUEST =
    CourseWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();

  private static final Pageable PAGEABLE = new PageRequest(0, 5);

  private static final Page<Course> COURSE_PAGE = new PageImpl<>(
    Collections.singletonList(COURSE), PAGEABLE, 1);

  private static final String URL_PREFIX = "url-prefix";

  private static final PagingResponse<CourseWebResponse> PAGING_RESPONSE =
    CourseResponseMapper.toCoursesPagingResponse(COURSE_PAGE, URL_PREFIX);

  private static final DataResponse<CourseWebResponse>
    RETRIEVED_COURSE_WEB_RESPONSE = CourseResponseMapper.toCourseDataResponse(
    COURSE, URL_PREFIX);

  private static final DataResponse<CourseWebResponse>
    CREATED_COURSE_WEB_RESPONSE = CourseResponseMapper.toCourseDataResponse(
    HttpStatus.CREATED, COURSE, URL_PREFIX);

  private JacksonTester<CourseWebRequest> courseWebRequestJacksonTester;

  @MockBean
  private CourseService courseService;

  @MockBean
  private CourseRequestMapper courseRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  @Override
  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.JUDGE);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      courseService, courseRequestMapper, fileProperties);
  }

  @Test
  public void testGivenApiCallByCreatingCourseReturnDataResponseObject()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(courseRequestMapper.toCourse(COURSE_WEB_REQUEST)).thenReturn(COURSE);
    when(courseService.createCourse(COURSE)).thenReturn(COURSE);

    mockMvc.perform(post("/api/core/courses").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        courseWebRequestJacksonTester.write(COURSE_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_COURSE_WEB_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(courseRequestMapper).toCourse(COURSE_WEB_REQUEST);
    verify(courseService).createCourse(COURSE);
  }

  @Test
  public void testGivenApiCallAndCourseIdByUpdatingCourseReturnDataResponseObject()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(courseRequestMapper.toCourse(ID, COURSE_WEB_REQUEST)).thenReturn(
      COURSE);
    when(courseService.updateCourse(COURSE)).thenReturn(COURSE);

    mockMvc.perform(put("/api/core/courses/" + ID).cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        courseWebRequestJacksonTester.write(COURSE_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_COURSE_WEB_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(courseRequestMapper).toCourse(ID, COURSE_WEB_REQUEST);
    verify(courseService).updateCourse(COURSE);
  }

  @Test
  public void testGivenApiCallAndCourseIdByDeletingCourseReturnBaseResponseObject()
    throws Exception {

    mockMvc.perform(delete("/api/core/courses/" + ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));

    verify(courseService).deleteCourse(ID);
    verifyZeroInteractions(courseRequestMapper, fileProperties);
  }

  @Test
  public void testGivenApiCallByGettingCourseReturnDataResponseObject()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(courseService.getCourses(PAGEABLE)).thenReturn(COURSE_PAGE);

    mockMvc.perform(get("/api/core/courses").cookie(cookies)
                      .param("page", "1")
                      .param("size", "5"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(courseService).getCourses(PAGEABLE);
    verifyZeroInteractions(courseRequestMapper);
  }

  @Test
  public void testGivenApiCallAndCourseIdByGettingCourseReturnDataResponseObject()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(courseService.getCourse(ID)).thenReturn(COURSE);

    mockMvc.perform(get("/api/core/courses/" + ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_COURSE_WEB_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(courseService).getCourse(ID);
    verifyZeroInteractions(courseRequestMapper);
  }

}
