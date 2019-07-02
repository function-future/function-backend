package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.core.DiscussionWebResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscussionResponseMapperTest {
  
  private static final String ID = "id";
  
  private static final String COURSE_ID = "course-id";
  
  private static final String DESCRIPTION = "description";
  
  private static final String USER_ID = "user-id";
  
  private static final String NAME = "name";
  
  private static final User USER = User.builder()
    .id(USER_ID)
    .name(NAME)
    .build();
  
  private static final long CREATED_AT = 1L;
  
  private static final DiscussionWebResponse DISCUSSION_WEB_RESPONSE =
    DiscussionWebResponse.builder()
      .id(ID)
      .author(new DiscussionWebResponse.Author(USER_ID, NAME))
      .comment(DESCRIPTION)
      .createdAt(CREATED_AT)
      .build();
  
  private static final DataResponse<DiscussionWebResponse> DATA_RESPONSE =
    DataResponse.<DiscussionWebResponse>builder().code(201)
      .status("CREATED")
      .data(DISCUSSION_WEB_RESPONSE)
      .build();
  
  private static final Paging PAGING = Paging.builder()
    .page(1)
    .size(4)
    .totalRecords(1)
    .build();
  
  private static final PagingResponse<DiscussionWebResponse> PAGING_RESPONSE =
    PagingResponse.<DiscussionWebResponse>builder().code(200)
      .status("OK")
      .data(Collections.singletonList(DISCUSSION_WEB_RESPONSE))
      .paging(PAGING)
      .build();
  
  private Discussion discussion;
  
  @Before
  public void setUp() {
    
    discussion = Discussion.builder()
      .id(ID)
      .courseId(COURSE_ID)
      .description(DESCRIPTION)
      .user(USER)
      .build();
    discussion.setCreatedAt(CREATED_AT);
  }
  
  @Test
  public void testGivenDiscussionByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<DiscussionWebResponse> dataResponse =
      DiscussionResponseMapper.toDiscussionDataResponse(discussion);
    
    assertThat(dataResponse).isEqualTo(DATA_RESPONSE);
  }
  
  @Test
  public void testGivenPageOfDiscussionByMappingToPagingResponseReturnPagingResponseObject() {
    
    Page<Discussion> discussions = new PageImpl<>(
      Collections.singletonList(discussion), new PageRequest(0, 4), 1);
    
    PagingResponse<DiscussionWebResponse> pagingResponse =
      DiscussionResponseMapper.toDiscussionPagingResponse(discussions);
    
    assertThat(pagingResponse).isEqualTo(PAGING_RESPONSE);
  }
  
}