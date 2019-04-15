package com.future.function.web.mapper.request.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Mapper class for incoming request for sticky note feature.
 */
@Slf4j
@Component
public class StickyNoteRequestMapper {
  
  private final ObjectMapper objectMapper;
  
  private final ObjectValidator validator;
  
  @Autowired
  public StickyNoteRequestMapper(
    ObjectMapper objectMapper, ObjectValidator validator
  ) {
    
    this.objectMapper = objectMapper;
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
    
    return toValidatedStickyNote(toStickyNoteWebRequest(data));
  }
  
  private StickyNote toValidatedStickyNote(StickyNoteWebRequest request) {
    
    StickyNote stickyNote = StickyNote.builder()
      .title(request.getNoteTitle())
      .description(request.getNoteDescription())
      .build();
    
    return validator.validate(stickyNote);
  }
  
  private StickyNoteWebRequest toStickyNoteWebRequest(String data) {
    
    StickyNoteWebRequest request;
    try {
      request = objectMapper.readValue(data, StickyNoteWebRequest.class);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: '{}'", e);
      throw new BadRequestException("Bad Request");
    }
    return request;
  }
  
}
