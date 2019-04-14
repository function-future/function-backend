package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;

/**
 * Service interface class for sticky note logic operations declaration.
 */
public interface StickyNoteService {
  
  /**
   * Retrieves latest sticky note from the database. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @return {@code StickyNote} - The sticky note object found in database
   */
  StickyNote getStickyNote();
  
  /**
   * Saves a new sticky note to the database.
   *
   * @param stickyNote Sticky note data of new sticky note.
   *
   * @return {@code StickyNote} - The sticky note object of the saved data.
   */
  StickyNote createStickyNote(StickyNote stickyNote);
  
}
