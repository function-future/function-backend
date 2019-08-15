package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.SharedCourseService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.DiscussionRequestMapper;
import com.future.function.web.mapper.request.core.SharedCourseRequestMapper;
import com.future.function.web.mapper.response.core.CourseResponseMapper;
import com.future.function.web.mapper.response.core.DiscussionResponseMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.DiscussionWebRequest;
import com.future.function.web.model.request.core.SharedCourseWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import com.future.function.web.model.response.feature.core.DiscussionWebResponse;
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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

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
public class SharedCourseControllerTest extends TestHelper {

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

  private static final String URL_PREFIX = "url-prefix";

  private static final DataResponse<CourseWebResponse> RETRIEVED_DATA_RESPONSE =
    CourseResponseMapper.toCourseDataResponse(COURSE, URL_PREFIX);

  private static final PagingResponse<CourseWebResponse> PAGING_RESPONSE =
    CourseResponseMapper.toCoursesPagingResponse(COURSE_PAGE, URL_PREFIX);

  private static final DataResponse<List<CourseWebResponse>>
    CREATED_DATA_RESPONSE = CourseResponseMapper.toCoursesDataResponse(
    COURSES, URL_PREFIX);

  private static final SharedCourseWebRequest SHARED_COURSE_WEB_REQUEST =
    SharedCourseWebRequest.builder()
      .targetBatch(BATCH_CODE)
      .originBatch(ORIGIN_BATCH_CODE)
      .courses(COURSE_IDS)
      .build();

  private static final String ID = "id";

  private static final String COMMENT = "text";

  private static final String USER_ID = "user-id";

  private static final String NAME = "name";

  private static final DiscussionWebRequest DISCUSSION_WEB_REQUEST =
    DiscussionWebRequest.builder()
      .comment(COMMENT)
      .build();

  private static final long CREATED_AT = 1L;

  private Discussion discussionFromRequest;

  private Discussion discussion;

  private Page<Discussion> discussions;

  private DataResponse<DiscussionWebResponse> discussionWebResponseDataResponse;

  private PagingResponse<DiscussionWebResponse>
    discussionWebResponsePagingResponse;

  private JacksonTester<CourseWebRequest> courseWebRequestJacksonTester;

  private JacksonTester<SharedCourseWebRequest>
    sharedCourseWebRequestJacksonTester;

  private JacksonTester<DiscussionWebRequest> discussionWebRequestJacksonTester;

  @MockBean
  private SharedCourseService sharedCourseService;

  @MockBean
  private SharedCourseRequestMapper sharedCourseRequestMapper;

  @MockBean
  private DiscussionRequestMapper discussionRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  @Override
  @Before
  public void setUp() {

    discussionFromRequest = Discussion.builder()
      .id(ID)
      .courseId(COURSE_ID)
      .batchCode(BATCH_CODE)
      .description(COMMENT)
      .user(this.buildUser(null, null))
      .build();

    discussion = Discussion.builder()
      .id(ID)
      .courseId(COURSE_ID)
      .batchCode(BATCH_CODE)
      .description(COMMENT)
      .user(this.buildUser(USER_ID, NAME))
      .build();
    discussion.setCreatedAt(CREATED_AT);

    discussions = new PageImpl<>(
      Collections.singletonList(discussion), PAGEABLE, 1);

    discussionWebResponseDataResponse =
      DiscussionResponseMapper.toDiscussionDataResponse(discussion);

    discussionWebResponsePagingResponse =
      DiscussionResponseMapper.toDiscussionPagingResponse(discussions);

    super.setUp();
    super.setCookie(Role.JUDGE);
  }

  private User buildUser(String id, String name) {

    return User.builder()
      .id(id)
      .email(JUDGE_EMAIL)
      .name(name)
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      sharedCourseService, sharedCourseRequestMapper, fileProperties);
  }

  @Test
  public void testGivenApiCallByDeletingCourseForBatchReturnBaseResponse()
    throws Exception {

    mockMvc.perform(delete(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID).cookie(
      cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));

    verify(sharedCourseService).deleteCourseByIdAndBatchCode(COURSE_ID,
                                                             BATCH_CODE
    );
    verifyZeroInteractions(sharedCourseRequestMapper, fileProperties);
  }

  @Test
  public void testGivenApiCallByGettingCourseForBatchReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(sharedCourseService.getCourseByIdAndBatchCode(COURSE_ID,
                                                       BATCH_CODE
    )).thenReturn(COURSE);

    mockMvc.perform(
      get("/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID).cookie(
        cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(sharedCourseService).getCourseByIdAndBatchCode(COURSE_ID,
                                                          BATCH_CODE
    );
    verifyZeroInteractions(sharedCourseRequestMapper);
  }

  @Test
  public void testGivenApiCallByGettingCoursesForBatchReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(sharedCourseService.getCoursesByBatchCode(BATCH_CODE,
                                                   PAGEABLE
    )).thenReturn(COURSE_PAGE);

    mockMvc.perform(
      get("/api/core/batches/" + BATCH_CODE + "/courses").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(sharedCourseService).getCoursesByBatchCode(BATCH_CODE, PAGEABLE);
    verifyZeroInteractions(sharedCourseRequestMapper);
  }

  @Test
  public void testGivenApiCallByUpdatingCourseForBatchReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(sharedCourseRequestMapper.toCourse(COURSE_ID,
                                            COURSE_WEB_REQUEST
    )).thenReturn(COURSE_FROM_REQUEST_MAPPER);
    when(sharedCourseService.updateCourseForBatch(COURSE_ID, BATCH_CODE,
                                                  COURSE_FROM_REQUEST_MAPPER
    )).thenReturn(COURSE);

    mockMvc.perform(put(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID).cookie(
      cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        courseWebRequestJacksonTester.write(COURSE_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(sharedCourseRequestMapper).toCourse(COURSE_ID, COURSE_WEB_REQUEST);
    verify(sharedCourseService).updateCourseForBatch(COURSE_ID, BATCH_CODE,
                                                     COURSE_FROM_REQUEST_MAPPER
    );
  }

  @Test
  public void testGivenApiCallByCreatingCoursesForBatchReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    Pair<List<String>, String> courseIdsAndOriginBatchCodePair = Pair.of(
      COURSE_IDS, ORIGIN_BATCH_CODE);
    when(sharedCourseRequestMapper.toCourseIdsAndOriginBatchCodePair(
      SHARED_COURSE_WEB_REQUEST)).thenReturn(courseIdsAndOriginBatchCodePair);
    when(sharedCourseService.createCourseForBatch(COURSE_IDS, ORIGIN_BATCH_CODE,
                                                  BATCH_CODE
    )).thenReturn(COURSES);

    mockMvc.perform(post("/api/core/batches/" + BATCH_CODE + "/courses").cookie(
      cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(sharedCourseWebRequestJacksonTester.write(
                        SHARED_COURSE_WEB_REQUEST)
                                 .getJson()))
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(sharedCourseRequestMapper).toCourseIdsAndOriginBatchCodePair(
      SHARED_COURSE_WEB_REQUEST);
    verify(sharedCourseService).createCourseForBatch(COURSE_IDS,
                                                     ORIGIN_BATCH_CODE,
                                                     BATCH_CODE
    );
  }

  @Test
  public void testGivenApiCallByGettingDiscussionsReturnPagingResponseObject()
    throws Exception {

    when(sharedCourseService.getDiscussions(JUDGE_EMAIL, COURSE_ID, BATCH_CODE,
                                            PAGEABLE
    )).thenReturn(discussions);

    mockMvc.perform(get(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID +
      "/discussions").param("size", "5")
                      .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(discussionWebResponsePagingResponse)
          .getJson()));

    verify(sharedCourseService).getDiscussions(
      JUDGE_EMAIL, COURSE_ID, BATCH_CODE, PAGEABLE);
    verifyZeroInteractions(fileProperties);
  }

  @Test
  public void testGivenApiCallByCreatingDiscussionReturnDataResponseObject()
    throws Exception {

    when(
      discussionRequestMapper.toDiscussion(DISCUSSION_WEB_REQUEST, JUDGE_EMAIL,
                                           COURSE_ID, BATCH_CODE
      )).thenReturn(discussionFromRequest);
    when(
      sharedCourseService.createDiscussion(discussionFromRequest)).thenReturn(
      discussion);

    mockMvc.perform(post(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID +
      "/discussions").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(discussionWebRequestJacksonTester.write(
                        DISCUSSION_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(discussionWebResponseDataResponse)
          .getJson()));

    verify(discussionRequestMapper).toDiscussion(
      DISCUSSION_WEB_REQUEST, JUDGE_EMAIL, COURSE_ID, BATCH_CODE);
    verify(sharedCourseService).createDiscussion(discussionFromRequest);
    verifyZeroInteractions(fileProperties);
  }

}
