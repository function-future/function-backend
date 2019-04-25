package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper class for incoming request for sticky note feature.
 */
@Slf4j
@Component
public class StickyNoteRequestMapper {
  
  private final WebRequestMapper requestMapper;
  
  private final RequestValidator validator;
  
  @Autowired
  public StickyNoteRequestMapper(
    WebRequestMapper requestMapper, RequestValidator validator
  ) {
    
    this.requestMapper = requestMapper;
    this.validator = validator;
  }
  
  /**
   * Converts JSON data to {@code StickyNote} object.
   *
   * @param data JSON data (in form of String) to be converted.
   *
   * @return {@code StickyNote} - Converted sticky note object.
   */
  public StickyNote toStickyNote(String data) {
    
    return toValidatedStickyNote(
      requestMapper.toWebRequestObject(data, StickyNoteWebRequest.class));
  }
  
  private StickyNote toValidatedStickyNote(StickyNoteWebRequest request) {
    
    validator.validate(request);
    
    return StickyNote.builder()
      .title(request.getNoteTitle())
      .description(request.getNoteDescription())
      .build();
  }
  
}
