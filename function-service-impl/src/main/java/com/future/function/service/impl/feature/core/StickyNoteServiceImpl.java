package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.repository.feature.core.StickyNoteRepository;
import com.future.function.service.api.feature.core.StickyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public StickyNote getStickyNote() {
    
    return stickyNoteRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()
      .orElseThrow(() -> new NotFoundException("Get Sticky Note Not Found"));
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
    
    stickyNoteRepository.save(stickyNote);
    
    return getStickyNote();
  }
  
}
