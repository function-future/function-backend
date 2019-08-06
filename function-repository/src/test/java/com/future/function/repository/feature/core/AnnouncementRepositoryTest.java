package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class AnnouncementRepositoryTest {
  
  @Autowired
  private AnnouncementRepository announcementRepository;
  
  private Announcement announcement1 = Announcement.builder()
    .title("title-1")
    .build();
  
  private Announcement announcement2 = Announcement.builder()
    .title("title-2")
    .build();
  
  @Before
  public void setUp() {
    
    announcement1.setUpdatedAt(10L);
    announcementRepository.save(announcement1);
    announcement2.setUpdatedAt(20L);
    announcementRepository.save(announcement2);
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenPageableByFindingAllSortedByUpdatedAtDescReturnPageOfAnnouncement() {
    
    assertThat(announcementRepository.findAllByOrderByUpdatedAtDesc(
      new PageRequest(0, 10))
                 .getContent()).isEqualTo(
      Arrays.asList(announcement2, announcement1));
  }
  
}
