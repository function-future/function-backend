package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.ActivityBlogWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityBlogRequestMapperTest {

  private static final String EMAIL = "email";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final String FILE_ID = "file-id";

  private static final List<String> FILE_IDS = Collections.singletonList(
    FILE_ID);

  private static final String ID = "id";

  private static final ActivityBlogWebRequest REQUEST =
    ActivityBlogWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .files(FILE_IDS)
      .build();

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private ActivityBlogRequestMapper activityBlogRequestMapper;

  @Before
  public void setUp() {}

  @After
  public void tearDown() {

    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testGivenEmailAndActivityBlogWebRequestByParsingToActivityBlogClassReturnActivityBlog() {

    when(validator.validate(REQUEST)).thenReturn(REQUEST);

    ActivityBlog expectedActivityBlog = ActivityBlog.builder()
      .user(User.builder()
              .email(EMAIL)
              .build())
      .title(TITLE)
      .description(DESCRIPTION)
      .files(Collections.singletonList(FileV2.builder()
                                         .id(FILE_ID)
                                         .build()))
      .build();

    ActivityBlog activityBlog = activityBlogRequestMapper.toActivityBlog(
      EMAIL, REQUEST);

    assertThat(activityBlog).isNotNull();
    assertThat(activityBlog).isEqualTo(expectedActivityBlog);

    verify(validator).validate(REQUEST);
  }

  @Test
  public void testGivenEmailAndActivityBlogIdActivityBlogWebRequestByParsingToActivityBlogClassReturnActivityBlog() {

    when(validator.validate(REQUEST)).thenReturn(REQUEST);

    ActivityBlog expectedActivityBlog = ActivityBlog.builder()
      .id(ID)
      .user(User.builder()
              .email(EMAIL)
              .build())
      .title(TITLE)
      .description(DESCRIPTION)
      .files(Collections.singletonList(FileV2.builder()
                                         .id(FILE_ID)
                                         .build()))
      .build();

    ActivityBlog activityBlog = activityBlogRequestMapper.toActivityBlog(
      EMAIL, ID, REQUEST);

    assertThat(activityBlog).isNotNull();
    assertThat(activityBlog).isEqualTo(expectedActivityBlog);

    verify(validator).validate(REQUEST);
  }

}
