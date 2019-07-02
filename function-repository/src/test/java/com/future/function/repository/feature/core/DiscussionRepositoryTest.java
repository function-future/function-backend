package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class DiscussionRepositoryTest {
  
  private static final String COURSE_ID = "course-id";
  
  private static final String BATCH_ID = "batch-id";
  
  @Autowired
  private DiscussionRepository discussionRepository;
  
  @Before
  public void setUp() {
    
    Discussion discussion = Discussion.builder()
      .courseId(COURSE_ID)
      .batchId(BATCH_ID)
      .build();
    
    discussionRepository.save(discussion);
  }
  
  @After
  public void tearDown() {
    
    discussionRepository.deleteAll();
  }
  
  @Test
  public void testGivenMethodCallByFindingDiscussionsByCourseIdAndBatchCodeReturnPageOfDiscussion() {
    
    Pageable pageable = new PageRequest(0, 10);
    Page<Discussion> discussions =
      discussionRepository.findAllByCourseIdAndBatchIdOrderByCreatedAtDesc(
        COURSE_ID, BATCH_ID, pageable);
    
    assertThat(discussions).isNotNull();
    assertThat(discussions.getContent()).isNotEmpty();
    assertThat(discussions.getContent()
                 .get(0)
                 .getCourseId()).isEqualTo(COURSE_ID);
    assertThat(discussions.getContent()
                 .get(0)
                 .getBatchId()).isEqualTo(BATCH_ID);
  }
  
}