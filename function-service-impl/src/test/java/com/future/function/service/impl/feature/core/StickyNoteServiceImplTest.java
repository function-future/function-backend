package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.repository.feature.core.StickyNoteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StickyNoteServiceImplTest {
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
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
    
    when(
      stickyNoteRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.of(stickyNote));
    StickyNote foundStickyNote = stickyNoteService.getStickyNote();
    
    assertThat(foundStickyNote).isNotNull();
    assertThat(foundStickyNote).isEqualTo(stickyNote);
    
    verify(stickyNoteRepository).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  }
  
  @Test
  public void testGivenNoStickyNoteInDatabaseAndMethodCallToGetStickyNoteByGettingStickyNoteReturnNotFoundException() {
    
    when(
      stickyNoteRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()).thenReturn(
      Optional.empty());
    
    catchException(() -> stickyNoteService.getStickyNote());
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Sticky Note Not Found");
    
    verify(stickyNoteRepository).findFirstByIdIsNotNullOrderByUpdatedAtDesc();
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
