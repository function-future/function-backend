package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.service.api.feature.core.CourseService;
import com.future.function.web.JacksonTestHelper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
@WebMvcTest(CourseController.class)
public class CourseControllerTest extends JacksonTestHelper {
  
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
  
  private static final PagingResponse<CourseWebResponse> PAGING_RESPONSE =
    CourseResponseMapper.toCoursesPagingResponse(COURSE_PAGE);
  
  private static final DataResponse<CourseWebResponse>
    RETRIEVED_COURSE_WEB_RESPONSE = CourseResponseMapper.toCourseDataResponse(
    COURSE);
  
  private static final DataResponse<CourseWebResponse>
    CREATED_COURSE_WEB_RESPONSE = CourseResponseMapper.toCourseDataResponse(
    HttpStatus.CREATED, COURSE);
  
  private JacksonTester<CourseWebRequest> courseWebRequestV2JacksonTester;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private CourseService courseService;
  
  @MockBean
  private CourseRequestMapper courseRequestMapper;
  
  @Before
  public void setUp() {
    
    super.setUp();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(courseService, courseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByCreatingCourseReturnDataResponseObject()
    throws Exception {
    
    when(courseRequestMapper.toCourse(COURSE_WEB_REQUEST)).thenReturn(COURSE);
    when(courseService.createCourse(COURSE)).thenReturn(COURSE);
    
    mockMvc.perform(post("/api/core/courses").contentType(
      MediaType.APPLICATION_JSON)
                      .content(courseWebRequestV2JacksonTester.write(
                        COURSE_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_COURSE_WEB_RESPONSE)
          .getJson()));
    
    verify(courseRequestMapper).toCourse(COURSE_WEB_REQUEST);
    verify(courseService).createCourse(COURSE);
  }
  
  @Test
  public void testGivenApiCallAndCourseIdByUpdatingCourseReturnDataResponseObject()
    throws Exception {
    
    when(courseRequestMapper.toCourse(ID, COURSE_WEB_REQUEST)).thenReturn(
      COURSE);
    when(courseService.updateCourse(COURSE)).thenReturn(COURSE);
    
    mockMvc.perform(put("/api/core/courses/" + ID).contentType(
      MediaType.APPLICATION_JSON)
                      .content(courseWebRequestV2JacksonTester.write(
                        COURSE_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_COURSE_WEB_RESPONSE)
          .getJson()));
    
    verify(courseRequestMapper).toCourse(ID, COURSE_WEB_REQUEST);
    verify(courseService).updateCourse(COURSE);
  }
  
  @Test
  public void testGivenApiCallAndCourseIdByDeletingCourseReturnBaseResponseObject()
    throws Exception {
    
    mockMvc.perform(delete("/api/core/courses/" + ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));
    
    verify(courseService).deleteCourse(ID);
    verifyZeroInteractions(courseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByGettingCourseReturnDataResponseObject()
    throws Exception {
    
    when(courseService.getCourses(PAGEABLE)).thenReturn(COURSE_PAGE);
    
    mockMvc.perform(get("/api/core/courses").param("page", "1")
                      .param("size", "5"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));
    
    verify(courseService).getCourses(PAGEABLE);
    verifyZeroInteractions(courseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallAndCourseIdByGettingCourseReturnDataResponseObject()
    throws Exception {
    
    when(courseService.getCourse(ID)).thenReturn(COURSE);
    
    mockMvc.perform(get("/api/core/courses/" + ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_COURSE_WEB_RESPONSE)
          .getJson()));
    
    verify(courseService).getCourse(ID);
    verifyZeroInteractions(courseRequestMapper);
  }
  
}
