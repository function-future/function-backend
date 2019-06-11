package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.DiscussionWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscussionRequestMapperTest {
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private DiscussionRequestMapper discussionRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenWebRequestAndParametersByParsingToDiscussionClassReturnDiscussionObject() {
    
    String comment = "comment";
    DiscussionWebRequest discussionWebRequest = DiscussionWebRequest.builder()
      .comment(comment)
      .build();
    String email = "email@email.com";
    String courseId = "course-id";
    String batchCode = "batch-code";
    
    when(validator.validate(discussionWebRequest)).thenReturn(
      discussionWebRequest);
    
    Discussion expectedDiscussion = Discussion.builder()
      .courseId(courseId)
      .batchCode(batchCode)
      .description(comment)
      .user(User.builder()
              .email(email)
              .build())
      .build();
    
    Discussion discussion = discussionRequestMapper.toDiscussion(
      discussionWebRequest, email, courseId, batchCode);
    
    assertThat(discussion).isNotNull();
    assertThat(discussion).isEqualTo(expectedDiscussion);
    
    verify(validator).validate(discussionWebRequest);
  }
  
}