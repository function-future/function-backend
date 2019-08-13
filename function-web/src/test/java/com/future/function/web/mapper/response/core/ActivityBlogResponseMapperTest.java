package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.core.embedded.AuthorWebResponseMapper;
import com.future.function.web.mapper.response.core.embedded.EmbeddedFileWebResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.ActivityBlogWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityBlogResponseMapperTest {

  private static final String ID = "id";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final String FILE_ID = "file-id";

  private static final FileV2 FILE_V2 = FileV2.builder()
    .id(FILE_ID)
    .build();

  private static final String USER_ID = "user-id";

  private static final String NAME = "name";

  private static final long UPDATED_AT = 10L;

  private static final User USER = User.builder()
    .id(USER_ID)
    .name(NAME)
    .build();

  private static final String URL_PREFIX = "url-prefix";

  private static final ActivityBlogWebResponse ACTIVITY_BLOG_WEB_RESPONSE =
    ActivityBlogWebResponse.builder()
      .id(ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .files(EmbeddedFileWebResponseMapper.toEmbeddedFileWebResponses(
        Collections.singletonList(FILE_V2), URL_PREFIX))
      .updatedAt(UPDATED_AT)
      .author(AuthorWebResponseMapper.buildAuthorWebResponse(USER))
      .build();

  private ActivityBlog activityBlog;

  @Before
  public void setUp() {

    activityBlog = ActivityBlog.builder()
      .id(ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .files(Collections.singletonList(FILE_V2))
      .user(USER)
      .build();
    activityBlog.setUpdatedAt(UPDATED_AT);
  }

  @After
  public void tearDown() {}

  @Test
  public void testGivenPageOfActivityBlogByMappingToPagingResponseReturnPagingResponseObject() {

    Pageable pageable = new PageRequest(0, 5);
    Page<ActivityBlog> activityBlogs = new PageImpl<>(
      Collections.singletonList(activityBlog), pageable, 1);

    PagingResponse<ActivityBlogWebResponse> expectedPagingResponse =
      PagingResponse.<ActivityBlogWebResponse>builder().
        code(200)
        .status("OK")
        .data(Collections.singletonList(ACTIVITY_BLOG_WEB_RESPONSE))
        .paging(PageHelper.toPaging(activityBlogs))
        .build();

    PagingResponse<ActivityBlogWebResponse> pagingResponse =
      ActivityBlogResponseMapper.toActivityBlogPagingResponse(activityBlogs,
                                                              URL_PREFIX
      );

    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(expectedPagingResponse);
  }

  @Test
  public void testGivenActivityBlogByMappingToDataResponseReturnDataResponseObject() {

    DataResponse<ActivityBlogWebResponse> expectedCreatedDataResponse =
      DataResponse.<ActivityBlogWebResponse>builder().code(201)
        .status("CREATED")
        .data(ACTIVITY_BLOG_WEB_RESPONSE)
        .build();

    DataResponse<ActivityBlogWebResponse> createdDataResponse =
      ActivityBlogResponseMapper.toActivityBlogDataResponse(
        HttpStatus.CREATED, activityBlog, URL_PREFIX);

    assertThat(createdDataResponse).isNotNull();
    assertThat(createdDataResponse).isEqualTo(expectedCreatedDataResponse);

    DataResponse<ActivityBlogWebResponse> expectedRetrievedDataResponse =
      DataResponse.<ActivityBlogWebResponse>builder().code(200)
        .status("OK")
        .data(ACTIVITY_BLOG_WEB_RESPONSE)
        .build();

    DataResponse<ActivityBlogWebResponse> retrievedDataResponse =
      ActivityBlogResponseMapper.toActivityBlogDataResponse(activityBlog,
                                                            URL_PREFIX
      );

    assertThat(retrievedDataResponse).isNotNull();
    assertThat(retrievedDataResponse).isEqualTo(expectedRetrievedDataResponse);
  }

}
