package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StickyNoteRepository
  extends MongoRepository<StickyNote, String> {

  Optional<StickyNote> findFirstByIdIsNotNullOrderByUpdatedAtDesc();

  Page<StickyNote> findAllByIdIsNotNullOrderByUpdatedAtDesc(Pageable pageable);

}
