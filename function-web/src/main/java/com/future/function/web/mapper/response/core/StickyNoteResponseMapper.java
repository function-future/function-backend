package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for sticky note web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StickyNoteResponseMapper {
  
  /**
   * Converts a sticky note data to {@code StickyNoteWebResponse}, wrapped in
   * {@code DataResponse}.
   *
   * @param data Sticky note data to be converted to response.
   *
   * @return {@code DataResponse<StickyNoteWebResponse>} - The converted sticky
   * note data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.StickyNoteWebResponse}
   */
  public static PagingResponse<StickyNoteWebResponse> toStickyNotePagingResponse(
    Page<StickyNote> data
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toStickyNoteWebResponseList(data),
                                           PageHelper.toPaging(data)
    );
  }
  
  private static List<StickyNoteWebResponse> toStickyNoteWebResponseList(
    Page<StickyNote> data
  ) {
    
    return data.getContent()
      .stream()
      .map(StickyNoteResponseMapper::buildStickyNoteWebResponse)
      .collect(Collectors.toList());
  }
  
  private static StickyNoteWebResponse buildStickyNoteWebResponse(
    StickyNote stickyNote
  ) {
    
    return StickyNoteWebResponse.builder()
      .id(stickyNote.getId())
      .title(stickyNote.getTitle())
      .description(stickyNote.getDescription())
      .updatedAt(stickyNote.getUpdatedAt())
      .build();
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
    
    return DataResponse.<StickyNoteWebResponse>builder().code(
      httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(buildStickyNoteWebResponse(stickyNote))
      .build();
  }
  
}
