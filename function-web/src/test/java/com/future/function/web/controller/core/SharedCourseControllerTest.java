package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.service.api.feature.core.shared.SharedCourseService;
import com.future.function.web.JacksonTestHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.CourseRequestMapperV1;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequest;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
@WebMvcTest(SharedCourseController.class)
public class SharedCourseControllerTest extends JacksonTestHelper {
  
  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);
  
  private static final BaseResponse CREATED_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String BATCH_CODE_1 = "1L";
  
  private static final String BATCH_CODE_2 = "2L";
  
  private static final SharedCourseWebRequest SHARED_COURSE_WEB_REQUEST =
    new SharedCourseWebRequest(BATCH_CODE_1, BATCH_CODE_2);
  
  private static final String SHARED_COURSE_REQUEST_DATA =
    "{\"originBatch" + "\":1,\"targetBatch\":2}";
  
  private static final String COURSE_REQUEST_DATA =
    "{\"courseTitle" + "\":\"Course Title\"," + "\"courseDescription" +
    "\":\"Course Description\"," + "\"batchCodes\":[1]}";
  
  private static final Course COURSE = Course.builder()
    .id(ID)
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
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private SharedCourseService sharedCourseService;
  
  @MockBean
  private CourseRequestMapperV1 courseRequestMapperV1;
  
  @Before
  public void setUp() {
    
    super.setUp();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(sharedCourseService, courseRequestMapperV1);
  }
  
  @Test
  public void testGivenCourseIdAndBatchNumberByDeletingCourseReturnBaseResponseObject()
    throws Exception {
    
    mockMvc.perform(delete("/api/core/courses/" + ID).param("batch",
                                                            String.valueOf(
                                                              BATCH_CODE_1)
    ))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));
    
    verify(sharedCourseService).deleteCourse(ID, BATCH_CODE_1);
    verifyZeroInteractions(courseRequestMapperV1);
  }
  
  @Test
  public void testGivenRequestBodyByCopyingCoursesReturnBaseResponseObject()
    throws Exception {
    
    List<String> batchNumbers = Arrays.asList(BATCH_CODE_1, BATCH_CODE_2);
    when(courseRequestMapperV1.toCopyCoursesData(
      SHARED_COURSE_WEB_REQUEST)).thenReturn(batchNumbers);
    
    mockMvc.perform(post("/api/core/courses/_copy").contentType(
      MediaType.APPLICATION_JSON)
                      .content(SHARED_COURSE_REQUEST_DATA))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        baseResponseJacksonTester.write(CREATED_BASE_RESPONSE)
          .getJson()));
    
    verify(courseRequestMapperV1).toCopyCoursesData(SHARED_COURSE_WEB_REQUEST);
    verify(sharedCourseService).copyCourses(
      batchNumbers.get(0), batchNumbers.get(1));
  }
  
  @Test
  public void testGivenCourseIdAndBatchNumberByGettingCourseReturnDataResponseObject()
    throws Exception {
    
    when(sharedCourseService.getCourse(ID, BATCH_CODE_1)).thenReturn(COURSE);
    
    mockMvc.perform(get("/api/core/courses/" + ID).param("batch",
                                                         String.valueOf(
                                                           BATCH_CODE_1)
    ))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_COURSE_WEB_RESPONSE)
          .getJson()));
    
    verify(sharedCourseService).getCourse(ID, BATCH_CODE_1);
  }
  
  @Test
  public void testGivenPageAndSizeAndBatchNumberByGettingCoursesReturnPagingResponseObject()
    throws Exception {
    
    when(sharedCourseService.getCourses(PAGEABLE, BATCH_CODE_1)).thenReturn(
      COURSE_PAGE);
    
    mockMvc.perform(get("/api/core/courses").param("page", "1")
                      .param("size", "5")
                      .param("batch", String.valueOf(BATCH_CODE_1)))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));
    
    verify(sharedCourseService).getCourses(PAGEABLE, BATCH_CODE_1);
  }
  
  @Test
  public void testGivenCourseDataAndFileByCreatingCourseReturnDataResponseObject()
    throws Exception {
    
    List<String> batchNumbers = Collections.singletonList(BATCH_CODE_1);
    Pair<Course, List<String>> pair = Pair.of(COURSE, batchNumbers);
    
    when(courseRequestMapperV1.toCourseAndBatchNumbers(
      COURSE_REQUEST_DATA)).thenReturn(pair);
    when(
      sharedCourseService.createCourse(COURSE, null, batchNumbers)).thenReturn(
      COURSE);
    
    mockMvc.perform(post("/api/core/courses").contentType(
      MediaType.MULTIPART_FORM_DATA_VALUE)
                      .param("data", COURSE_REQUEST_DATA)
                      .param("file", ""))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_COURSE_WEB_RESPONSE)
          .getJson()));
    
    verify(courseRequestMapperV1).toCourseAndBatchNumbers(COURSE_REQUEST_DATA);
    verify(sharedCourseService).createCourse(COURSE, null, batchNumbers);
  }
  
  @Test
  public void testGivenCourseIdAndDataAndFileByUpdatingCourseReturnDataResponseObject()
    throws Exception {
    
    List<String> batchNumbers = Arrays.asList(BATCH_CODE_1, BATCH_CODE_2);
    Pair<Course, List<String>> pair = Pair.of(COURSE, batchNumbers);
    
    when(courseRequestMapperV1.toCourseAndBatchNumbers(ID,
                                                       COURSE_REQUEST_DATA
    )).thenReturn(pair);
    when(
      sharedCourseService.updateCourse(COURSE, null, batchNumbers)).thenReturn(
      COURSE);
    
    mockMvc.perform(put("/api/core/courses/" + ID).contentType(
      MediaType.MULTIPART_FORM_DATA_VALUE)
                      .param("data", COURSE_REQUEST_DATA)
                      .param("file", ""))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_COURSE_WEB_RESPONSE)
          .getJson()));
    
    verify(courseRequestMapperV1).toCourseAndBatchNumbers(ID,
                                                          COURSE_REQUEST_DATA
    );
    verify(sharedCourseService).updateCourse(COURSE, null, batchNumbers);
  }
  
}