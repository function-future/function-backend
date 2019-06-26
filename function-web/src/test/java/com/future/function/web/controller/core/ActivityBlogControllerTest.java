package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.ActivityBlogService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.ActivityBlogRequestMapper;
import com.future.function.web.mapper.response.core.ActivityBlogResponseMapper;
import com.future.function.web.model.request.core.ActivityBlogWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.ActivityBlogWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
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
@WebMvcTest(ActivityBlogController.class)
public class ActivityBlogControllerTest extends TestHelper {
  
  private static final int PAGE = 1;
  
  private static final int SIZE = 5;
  
  private static final Pageable PAGEABLE = PageHelper.toPageable(PAGE, SIZE);
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String USER_ID = "user-id";
  
  private static final String NAME = "name";
  
  private static final String EMAIL = "email";
  
  private static final User USER = User.builder()
    .id(USER_ID)
    .name(NAME)
    .email(EMAIL)
    .build();
  
  private static final ActivityBlog ACTIVITY_BLOG = ActivityBlog.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .user(USER)
    .build();
  
  private static final PageImpl<ActivityBlog> ACTIVITY_BLOG_PAGE =
    new PageImpl<>(Collections.singletonList(ACTIVITY_BLOG), PAGEABLE, 1);
  
  private static final DataResponse<ActivityBlogWebResponse>
    RETRIEVED_DATA_RESPONSE =
    ActivityBlogResponseMapper.toActivityBlogDataResponse(ACTIVITY_BLOG);
  
  private static final DataResponse<ActivityBlogWebResponse>
    CREATED_DATA_RESPONSE =
    ActivityBlogResponseMapper.toActivityBlogDataResponse(HttpStatus.CREATED,
                                                          ACTIVITY_BLOG
    );
  
  private static final PagingResponse<ActivityBlogWebResponse> PAGING_RESPONSE =
    ActivityBlogResponseMapper.toActivityBlogPagingResponse(ACTIVITY_BLOG_PAGE);
  
  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);
  
  private static final ActivityBlogWebRequest REQUEST =
    ActivityBlogWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .files(Collections.emptyList())
      .build();
  
  private JacksonTester<ActivityBlogWebRequest>
    activityBlogWebRequestJacksonTester;
  
  @MockBean
  private ActivityBlogService activityBlogService;
  
  @MockBean
  private ActivityBlogRequestMapper activityBlogRequestMapper;
  
  @Override
  @Before
  public void setUp() {
    
    super.setUp();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(activityBlogService, activityBlogRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByGettingActivityBlogsReturnPagingResponse()
    throws Exception {
    
    when(activityBlogService.getActivityBlogs("", "", PAGEABLE)).thenReturn(
      ACTIVITY_BLOG_PAGE);
    
    mockMvc.perform(get("/api/core/activity-blogs"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));
    
    verify(activityBlogService).getActivityBlogs("", "", PAGEABLE);
    verifyZeroInteractions(activityBlogRequestMapper);
  }
  
  @Test
  public void testGivenApiCallAndActivityBlogIdByGettingActivityBlogReturnDataResponse()
    throws Exception {
    
    when(activityBlogService.getActivityBlog(ID)).thenReturn(ACTIVITY_BLOG);
    
    mockMvc.perform(get("/api/core/activity-blogs/" + ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));
    
    verify(activityBlogService).getActivityBlog(ID);
    
    verifyZeroInteractions(activityBlogRequestMapper);
  }
  
  @Test
  public void testGivenApiCallAndActivityBlogDataAndEmailByCreatingActivityBlogReturnDataResponse()
    throws Exception {
    
    super.setCookie(Role.STUDENT);
    
    when(activityBlogRequestMapper.toActivityBlog(STUDENT_EMAIL,
                                                  REQUEST
    )).thenReturn(ACTIVITY_BLOG);
    when(activityBlogService.createActivityBlog(ACTIVITY_BLOG)).thenReturn(
      ACTIVITY_BLOG);
    
    mockMvc.perform(post("/api/core/activity-blogs").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        activityBlogWebRequestJacksonTester.write(REQUEST)
                          .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));
    
    verify(activityBlogRequestMapper).toActivityBlog(STUDENT_EMAIL, REQUEST);
    verify(activityBlogService).createActivityBlog(ACTIVITY_BLOG);
  }
  
  @Test
  public void testGivenApiCallAndActivityBlogDataAndActivityBlogIdAndEmailByUpdatingActivityBlogReturnDataResponse()
    throws Exception {
    
    super.setCookie(Role.STUDENT);
    
    when(activityBlogRequestMapper.toActivityBlog(STUDENT_EMAIL, ID,
                                                  REQUEST
    )).thenReturn(ACTIVITY_BLOG);
    when(activityBlogService.updateActivityBlog(ACTIVITY_BLOG)).thenReturn(
      ACTIVITY_BLOG);
    
    mockMvc.perform(put("/api/core/activity-blogs/" + ID).cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(
                        activityBlogWebRequestJacksonTester.write(REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));
    
    verify(activityBlogRequestMapper).toActivityBlog(
      STUDENT_EMAIL, ID, REQUEST);
    verify(activityBlogService).updateActivityBlog(ACTIVITY_BLOG);
  }
  
  @Test
  public void testGivenApiCallAndActivityBlogIdAndEmailByDeletingActivityBlogReturnBaseResponse()
    throws Exception {
    
    super.setCookie(Role.STUDENT);
    
    mockMvc.perform(delete("/api/core/activity-blogs/" + ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));
    
    verify(activityBlogService).deleteActivityBlog(STUDENT_EMAIL, ID);
    verifyZeroInteractions(activityBlogRequestMapper);
  }
  
}
