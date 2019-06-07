package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.service.api.feature.core.SharedCourseService;
import com.future.function.web.JacksonTestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.SharedCourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.SharedCourseWebRequest;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = SharedCourseController.class)
public class SharedCourseControllerTest extends JacksonTestHelper {
  
  private static final String ORIGIN_BATCH_CODE = "origin-batch-code";
  
  private static final String COURSE_ID = "course-id";
  
  private static final List<String> COURSE_IDS = Collections.singletonList(
    COURSE_ID);
  
  private static final String COURSE_TITLE = "course-title";
  
  private static final String COURSE_DESCRIPTION = "course-description";
  
  private static final String FILE_URL = "file-url";
  
  private static final String THUMBNAIL_URL = "thumbnail-url";
  
  private static final String BATCH_CODE = "batch-code";
  
  private static final FileV2 FILE = FileV2.builder()
    .fileUrl(FILE_URL)
    .thumbnailUrl(THUMBNAIL_URL)
    .build();
  
  private static final Course COURSE = Course.builder()
    .id(COURSE_ID)
    .title(COURSE_TITLE)
    .description(COURSE_DESCRIPTION)
    .file(FILE)
    .build();
  
  private static final List<Course> COURSES = Collections.singletonList(COURSE);
  
  private static final Pageable PAGEABLE = new PageRequest(0, 5);
  
  private static final Page<Course> COURSE_PAGE = new PageImpl<>(
    COURSES, PAGEABLE, COURSES.size());
  
  private static final String MATERIAL = "material";
  
  private static final FileV2 FILE_FROM_REQUEST_MAPPER = FileV2.builder()
    .id(MATERIAL)
    .build();
  
  private static final Course COURSE_FROM_REQUEST_MAPPER = Course.builder()
    .id(COURSE_ID)
    .title(COURSE_TITLE)
    .description(COURSE_DESCRIPTION)
    .file(FILE_FROM_REQUEST_MAPPER)
    .build();
  
  private static final CourseWebRequest COURSE_WEB_REQUEST =
    CourseWebRequest.builder()
      .title(COURSE_TITLE)
      .description(COURSE_DESCRIPTION)
      .material(Collections.singletonList(MATERIAL))
      .build();
  
  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);
  
  private static final DataResponse<CourseWebResponse> RETRIEVED_DATA_RESPONSE =
    CourseResponseMapper.toCourseDataResponse(COURSE);
  
  private static final PagingResponse<CourseWebResponse> PAGING_RESPONSE =
    CourseResponseMapper.toCoursesPagingResponse(COURSE_PAGE);
  
  private static final DataResponse<List<CourseWebResponse>>
    CREATED_DATA_RESPONSE = CourseResponseMapper.toCoursesDataResponse(COURSES);
  
  private static final SharedCourseWebRequest SHARED_COURSE_WEB_REQUEST =
    SharedCourseWebRequest.builder()
      .originBatch(ORIGIN_BATCH_CODE)
      .courses(COURSE_IDS)
      .build();
  
  private JacksonTester<CourseWebRequest> courseWebRequestJacksonTester;
  
  @MockBean
  private SharedCourseService sharedCourseService;
  
  @MockBean
  private SharedCourseRequestMapper sharedCourseRequestMapper;
  
  @Autowired
  private MockMvc mockMvc;
  
  private JacksonTester<SharedCourseWebRequest>
    sharedCourseWebRequestJacksonTester;
  
  @Before
  public void setUp() {
    
    super.setUp();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(sharedCourseService, sharedCourseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByDeletingCourseForBatchReturnBaseResponse()
    throws Exception {
    
    mockMvc.perform(
      delete("/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));
    
    verify(sharedCourseService).deleteCourseByIdAndBatchCode(COURSE_ID,
                                                             BATCH_CODE
    );
    verifyZeroInteractions(sharedCourseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByGettingCourseForBatchReturnDataResponse()
    throws Exception {
    
    when(sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                       BATCH_CODE
    )).thenReturn(COURSE);
    
    mockMvc.perform(
      get("/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));
    
    verify(sharedCourseService).getCourseByIdAndBatchCode(COURSE_ID,
                                                          BATCH_CODE
    );
    verifyZeroInteractions(sharedCourseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByGettingCoursesForBatchReturnDataResponse()
    throws Exception {
    
    when(sharedCourseService.getCoursesByBatchCode(BATCH_CODE,
                                                   PAGEABLE
    )).thenReturn(COURSE_PAGE);
    
    mockMvc.perform(get("/api/core/batches/" + BATCH_CODE + "/courses"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));
    
    verify(sharedCourseService).getCoursesByBatchCode(BATCH_CODE, PAGEABLE);
    verifyZeroInteractions(sharedCourseRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByUpdatingCourseForBatchReturnDataResponse()
    throws Exception {
    
    when(sharedCourseRequestMapper.toCourse(COURSE_ID,
                                            COURSE_WEB_REQUEST
    )).thenReturn(COURSE_FROM_REQUEST_MAPPER);
    when(sharedCourseService.updateCourseForBatch(COURSE_ID, BATCH_CODE,
                                                  COURSE_FROM_REQUEST_MAPPER
    )).thenReturn(COURSE);
    
    mockMvc.perform(put(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID).contentType(
      MediaType.APPLICATION_JSON)
                      .content(
                        courseWebRequestJacksonTester.write(COURSE_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));
    
    verify(sharedCourseRequestMapper).toCourse(COURSE_ID, COURSE_WEB_REQUEST);
    verify(sharedCourseService).updateCourseForBatch(COURSE_ID, BATCH_CODE,
                                                     COURSE_FROM_REQUEST_MAPPER
    );
  }
  
  @Test
  public void testGivenApiCallByCreatingCoursesForBatchReturnDataResponse()
    throws Exception {
    
    Pair<List<String>, String> courseIdsAndOriginBatchCodePair = Pair.of(
      COURSE_IDS, ORIGIN_BATCH_CODE);
    when(sharedCourseRequestMapper.toCourseIdsAndOriginBatchCodePair(
      SHARED_COURSE_WEB_REQUEST)).thenReturn(courseIdsAndOriginBatchCodePair);
    when(sharedCourseService.createCourseForBatch(COURSE_IDS, ORIGIN_BATCH_CODE,
                                                  BATCH_CODE
    )).thenReturn(COURSES);
    
    mockMvc.perform(post(
      "/api/core/batches/" + BATCH_CODE + "/courses").contentType(
      MediaType.APPLICATION_JSON)
                      .content(sharedCourseWebRequestJacksonTester.write(
                        SHARED_COURSE_WEB_REQUEST)
                                 .getJson()))
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));
    
    verify(sharedCourseRequestMapper).toCourseIdsAndOriginBatchCodePair(
      SHARED_COURSE_WEB_REQUEST);
    verify(sharedCourseService).createCourseForBatch(COURSE_IDS,
                                                     ORIGIN_BATCH_CODE,
                                                     BATCH_CODE
    );
  }
  
}
