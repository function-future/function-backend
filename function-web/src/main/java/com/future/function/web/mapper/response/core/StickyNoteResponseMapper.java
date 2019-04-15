package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Mapper class for sticky note web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StickyNoteResponseMapper {
  
  /**
   * Converts a sticky note data to {@code StickyNoteWebResponse}, wrapped in
   * {@code DataResponse}.
   *
   * @param stickyNote Sticky note data to be converted to response.
   *
   * @return {@code DataResponse<StickyNoteWebResponse>} - The converted sticky
   * note data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.StickyNoteWebResponse}
   */
  public static DataResponse<StickyNoteWebResponse> toStickyNoteDataResponse(
    StickyNote stickyNote
  ) {
    
    return toStickyNoteDataResponse(HttpStatus.OK, stickyNote);
  }
  
  /**
   * Converts a sticky note data to {@code StickyNoteWebResponse}, wrapped in
   * {@code DataResponse}.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param stickyNote Sticky note data to be converted to response.
   *
   * @return {@code DataResponse<StickyNoteWebResponse>} - The converted sticky
   * note data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.StickyNoteWebResponse}
   */
  public static DataResponse<StickyNoteWebResponse> toStickyNoteDataResponse(
    HttpStatus httpStatus, StickyNote stickyNote
  ) {
    
    return DataResponse.<StickyNoteWebResponse>builder().code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(buildStickyNoteWebResponse(stickyNote))
      .build();
  }
  
  private static StickyNoteWebResponse buildStickyNoteWebResponse(
    StickyNote stickyNote
  ) {
    
    return StickyNoteWebResponse.builder()
      .noteTitle(stickyNote.getTitle())
      .noteDescription(stickyNote.getDescription())
      .updatedAt(stickyNote.getUpdatedAt())
      .build();
  }
  
}
