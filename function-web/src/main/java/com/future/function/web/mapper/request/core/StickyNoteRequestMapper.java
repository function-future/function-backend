package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StickyNoteRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public StickyNoteRequestMapper(
    RequestValidator validator
  ) {

    this.validator = validator;
  }

  public StickyNote toStickyNote(StickyNoteWebRequest request) {

    return toValidatedStickyNote(request);
  }

  private StickyNote toValidatedStickyNote(StickyNoteWebRequest request) {

    validator.validate(request);

    return StickyNote.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .build();
  }

}
