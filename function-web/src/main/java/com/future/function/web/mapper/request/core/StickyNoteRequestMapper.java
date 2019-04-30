package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper class for incoming request for sticky note feature.
 */
@Component
public class StickyNoteRequestMapper {
  
  private final RequestValidator validator;
  
  @Autowired
  public StickyNoteRequestMapper(
    RequestValidator validator
  ) {
    
    this.validator = validator;
  }
  
  /**
   * Converts Jackson-mapped JSON data to {@code StickyNote} object.
   *
   * @param request JSON data (in form of StickyNoteWebRequest) to be converted.
   *
   * @return {@code StickyNote} - Converted sticky note object.
   */
  public StickyNote toStickyNote(StickyNoteWebRequest request) {
  
    return toValidatedStickyNote(request);
  }
  
  private StickyNote toValidatedStickyNote(StickyNoteWebRequest request) {
    
    validator.validate(request);
    
    return StickyNote.builder()
      .title(request.getNoteTitle())
      .description(request.getNoteDescription())
      .build();
  }
  
}
