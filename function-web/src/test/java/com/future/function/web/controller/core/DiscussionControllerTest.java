package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.DiscussionService;
import com.future.function.web.JacksonTestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.request.core.DiscussionRequestMapper;
import com.future.function.web.mapper.response.core.DiscussionResponseMapper;
import com.future.function.web.model.request.core.DiscussionWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.DiscussionWebResponse;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = DiscussionController.class)
public class DiscussionControllerTest extends JacksonTestHelper {
  
  private static final String BATCH_CODE = "batch-code";
  
  private static final String COURSE_ID = "course-id";
  
  private static final String ID = "id";
  
  private static final String COMMENT = "comment";
  
  private static final String EMAIL = "email";
  
  private static final String USER_ID = "user-id";
  
  private static final String NAME = "name";
  
  private static final DiscussionWebRequest DISCUSSION_WEB_REQUEST =
    DiscussionWebRequest.builder()
      .comment(COMMENT)
      .build();
  
  private static final int PAGE = 1;
  
  private static final int SIZE = 4;
  
  private static final Pageable PAGEABLE = new PageRequest(PAGE - 1, SIZE);
  
  private static final long CREATED_AT = 1L;
  
  private Discussion discussionFromRequest;
  
  private Discussion discussion;
  
  private Page<Discussion> discussions;
  
  private DataResponse<DiscussionWebResponse> dataResponse;
  
  private PagingResponse<DiscussionWebResponse> pagingResponse;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private DiscussionService discussionService;
  
  @MockBean
  private DiscussionRequestMapper discussionRequestMapper;
  
  private JacksonTester<DiscussionWebRequest> discussionWebRequestJacksonTester;
  
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
    
    dataResponse = DiscussionResponseMapper.toDiscussionDataResponse(
      discussion);
    
    pagingResponse = DiscussionResponseMapper.toDiscussionPagingResponse(
      discussions);
    
    super.setUp();
  }
  
  private User buildUser(String id, String name) {
    
    return User.builder()
      .id(id)
      .email(EMAIL)
      .name(name)
      .build();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(discussionService, discussionRequestMapper);
  }
  
  @Test
  public void testGivenApiCallByGettingDiscussionsReturnPagingResponseObject()
    throws Exception {
    
    when(discussionService.getDiscussions(EMAIL, COURSE_ID, BATCH_CODE,
                                          PAGEABLE
    )).thenReturn(discussions);
    
    mockMvc.perform(get(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID +
      "/discussions").param("email", EMAIL))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(pagingResponse)
          .getJson()));
    
    verify(discussionService).getDiscussions(
      EMAIL, COURSE_ID, BATCH_CODE, PAGEABLE);
  }
  
  @Test
  public void testGivenApiCallByCreatingDiscussionReturnDataResponseObject()
    throws Exception {
    
    when(discussionRequestMapper.toDiscussion(DISCUSSION_WEB_REQUEST, EMAIL,
                                              COURSE_ID, BATCH_CODE
    )).thenReturn(discussionFromRequest);
    when(discussionService.createDiscussion(discussionFromRequest)).thenReturn(
      discussion);
    
    mockMvc.perform(post(
      "/api/core/batches/" + BATCH_CODE + "/courses/" + COURSE_ID +
      "/discussions").param("email", EMAIL)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(discussionWebRequestJacksonTester.write(
                        DISCUSSION_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(dataResponseJacksonTester.write(dataResponse)
                                  .getJson()));
    
    verify(discussionRequestMapper).toDiscussion(DISCUSSION_WEB_REQUEST, EMAIL,
                                                 COURSE_ID, BATCH_CODE
    );
    verify(discussionService).createDiscussion(discussionFromRequest);
  }
  
}
