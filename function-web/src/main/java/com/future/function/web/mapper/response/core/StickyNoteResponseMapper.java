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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StickyNoteResponseMapper {
  
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
