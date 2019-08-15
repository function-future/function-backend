package com.future.function.service.impl.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.repository.feature.core.StickyNoteRepository;
import com.future.function.service.impl.helper.PageHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StickyNoteServiceImplTest {

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final Pageable PAGEABLE = new PageRequest(0, 1);

  private StickyNote stickyNote;

  @Mock
  private StickyNoteRepository stickyNoteRepository;

  @InjectMocks
  private StickyNoteServiceImpl stickyNoteService;

  @Before
  public void setUp() {

    stickyNote = StickyNote.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(stickyNoteRepository);
  }

  @Test
  public void testGivenMethodCallToGetStickyNoteByGettingStickyNoteReturnStickyNoteObject() {

    Page<StickyNote> stickyNotePage = PageHelper.toPage(
      Collections.singletonList(stickyNote), PAGEABLE);
    when(stickyNoteRepository.findAllByIdIsNotNullOrderByUpdatedAtDesc(
      PAGEABLE)).thenReturn(stickyNotePage);

    Page<StickyNote> foundStickyNotePage = stickyNoteService.getStickyNote(
      PAGEABLE);

    assertThat(foundStickyNotePage).isNotNull();
    assertThat(foundStickyNotePage).isEqualTo(stickyNotePage);

    verify(stickyNoteRepository).findAllByIdIsNotNullOrderByUpdatedAtDesc(
      PAGEABLE);
  }

  @Test
  public void testGivenMethodToCreateStickyNoteByCreatingStickyNoteReturnNewStickyNoteObject() {

    when(stickyNoteRepository.save(stickyNote)).thenReturn(stickyNote);
    when(
      stickyNoteRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(stickyNote));

    StickyNote createdStickyNote = stickyNoteService.createStickyNote(
      stickyNote);

    assertThat(createdStickyNote).isNotNull();
    assertThat(createdStickyNote).isEqualTo(stickyNote);

    verify(stickyNoteRepository).save(stickyNote);
    verify(stickyNoteRepository).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  }

}
