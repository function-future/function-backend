package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StickyNoteResponseMapper {
  
  public static DataResponse<StickyNoteWebResponse> toStickyNoteDataResponse(
    StickyNote stickyNote
  ) {
    
    return toStickyNoteDataResponse(HttpStatus.OK, stickyNote);
  }
  
  public static DataResponse<StickyNoteWebResponse> toStickyNoteDataResponse(
    HttpStatus httpStatus, StickyNote stickyNote
  ) {
    
    return DataResponse.<StickyNoteWebResponse>builder().code(
      httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(buildStickyNoteWebResponse(stickyNote))
      .build();
  }
  
  private static StickyNoteWebResponse buildStickyNoteWebResponse(
    StickyNote stickyNote
  ) {
    
    return StickyNoteWebResponse.builder()
      .title(stickyNote.getTitle())
      .description(stickyNote.getDescription())
      .updatedAt(stickyNote.getUpdatedAt())
      .build();
  }
  
}
