package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.StickyNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StickyNoteService {

  Page<StickyNote> getStickyNote(Pageable pageable);

  StickyNote createStickyNote(StickyNote stickyNote);

}
