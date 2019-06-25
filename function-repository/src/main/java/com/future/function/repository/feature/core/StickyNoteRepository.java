package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository class for sticky note database operations.
 */
public interface StickyNoteRepository
  extends MongoRepository<StickyNote, String> {
  
  /**
   * Finds first (latest) sticky note in database based on {@code updatedAt}
   * field.
   *
   * @return {@code Optional<StickyNote>} - Sticky note found in database, if
   * any exists; otherwise returns {@link java.util.Optional#empty()}.
   */
  Optional<StickyNote> findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  
  Page<StickyNote> findAllByIdIsNotNullOrderByUpdatedAtDesc(Pageable pageable);
  
}
