package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class StickyNoteRepositoryTest {

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  @Autowired
  private StickyNoteRepository stickyNoteRepository;

  @Before
  public void setUp() {

    stickyNoteRepository.save(StickyNote.builder()
                                .title(TITLE)
                                .description(DESCRIPTION)
                                .build());
  }

  @After
  public void tearDown() {

    stickyNoteRepository.deleteAll();
  }

  @Test
  public void testGivenMethodCallByFindingFirstStickyNoteReturnStickyNoteObject() {

    Optional<StickyNote> foundStickyNote =
      stickyNoteRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc();

    assertThat(foundStickyNote).isNotEqualTo(Optional.empty());
    assertThat(foundStickyNote.get()
                 .getTitle()).isEqualTo(TITLE);
    assertThat(foundStickyNote.get()
                 .getDescription()).isEqualTo(DESCRIPTION);
  }

}
