package com.future.function.service.impl.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.repository.feature.core.StickyNoteRepository;
import com.future.function.service.api.feature.core.StickyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StickyNoteServiceImpl implements StickyNoteService {
  
  private final StickyNoteRepository stickyNoteRepository;
  
  @Autowired
  public StickyNoteServiceImpl(StickyNoteRepository stickyNoteRepository) {
    
    this.stickyNoteRepository = stickyNoteRepository;
  }
  
  /**
   * {@inheritDoc}
   *
   * @return {@code StickyNote} - The sticky note object found in database
   */
  @Override
  public Page<StickyNote> getStickyNote(Pageable pageable) {
    
    return stickyNoteRepository.findAllByIdIsNotNullOrderByUpdatedAtDesc(
      pageable);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param stickyNote Sticky note data of new sticky note.
   *
   * @return {@code StickyNote} - The sticky note object of the saved data.
   */
  @Override
  public StickyNote createStickyNote(StickyNote stickyNote) {
    
    return Optional.of(stickyNote)
      .map(stickyNoteRepository::save)
      .flatMap(
        ignored -> stickyNoteRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc())
      .orElseGet(StickyNote::new);
  }
  
}
