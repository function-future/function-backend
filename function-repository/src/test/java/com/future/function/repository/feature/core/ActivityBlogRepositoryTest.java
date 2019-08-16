package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ActivityBlogRepositoryTest {

  private static final PageRequest PAGEABLE = new PageRequest(0, 5);

  private static final String USER_ID = new ObjectId().toHexString();

  private static final User USER = User.builder()
    .id(USER_ID)
    .build();

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final ActivityBlog ACTIVITY_BLOG = ActivityBlog.builder()
    .user(USER)
    .title(TITLE)
    .description(DESCRIPTION)
    .build();

  private static final String USER_ID_2 = "user-id-2";

  private static final User USER_2 = User.builder()
    .id(USER_ID_2)
    .build();

  private static final String TITLE_2 = "har";

  private static final String DESCRIPTION_2 = "harhar";

  private static final ActivityBlog ACTIVITY_BLOG_2 = ActivityBlog.builder()
    .user(USER_2)
    .title(TITLE_2)
    .description(DESCRIPTION_2)
    .build();

  @Autowired
  private ActivityBlogRepository activityBlogRepository;

  @Before
  public void setUp() {

    activityBlogRepository.save(ACTIVITY_BLOG);
    activityBlogRepository.save(ACTIVITY_BLOG_2);
  }

  @After
  public void tearDown() {

    activityBlogRepository.deleteAll();
  }

  @Test
  public void testGivenEmptyUserIdAndEmptySearchKeyByFindingActivityBlogsReturnPageOfActivityBlog() {

    Page<ActivityBlog> activityBlogPage;

    activityBlogPage = activityBlogRepository.findAll("", "", PAGEABLE);

    assertThat(activityBlogPage).isNotNull();
    assertThat(activityBlogPage.getContent()).isNotEmpty();
    assertThat(activityBlogPage.getContent()
                 .size()).isEqualTo(2);

    activityBlogPage = activityBlogRepository.findAll("", "i", PAGEABLE);

    assertThat(activityBlogPage).isNotNull();
    assertThat(activityBlogPage.getContent()).isNotEmpty();
    assertThat(activityBlogPage.getContent()
                 .size()).isEqualTo(1);

    activityBlogPage = activityBlogRepository.findAll(USER_ID, "", PAGEABLE);

    assertThat(activityBlogPage).isNotNull();
    assertThat(activityBlogPage.getContent()).isNotEmpty();
    assertThat(activityBlogPage.getContent()
                 .size()).isEqualTo(1);

    activityBlogPage = activityBlogRepository.findAll(USER_ID, "i", PAGEABLE);

    assertThat(activityBlogPage).isNotNull();
    assertThat(activityBlogPage.getContent()).isNotEmpty();
    assertThat(activityBlogPage.getContent()
                 .size()).isEqualTo(1);
  }

}
