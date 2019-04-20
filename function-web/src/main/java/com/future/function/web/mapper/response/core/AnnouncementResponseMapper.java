package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.File;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper class for announcement web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnouncementResponseMapper {
  
  public static DataResponse<AnnouncementWebResponse> toAnnouncementDataResponse(
    Announcement announcement
  ) {
    
    return toAnnouncementDataResponse(HttpStatus.OK, announcement);
  }
  
  public static DataResponse<AnnouncementWebResponse> toAnnouncementDataResponse(
    HttpStatus httpStatus, Announcement announcement
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         buildAnnouncementWebResponse(
                                           announcement)
    );
  }
  
  private static AnnouncementWebResponse buildAnnouncementWebResponse(
    Announcement announcement
  ) {
    
    return AnnouncementWebResponse.builder()
      .announcementId(announcement.getId())
      .announcementTitle(announcement.getTitle())
      .announcementSummary(announcement.getSummary())
      .announcementDescriptionHtml(announcement.getDescriptionHtml())
      .announcementFileUrl(getFileUrl(announcement))
      .createdAt(announcement.getCreatedAt())
      .updatedAt(announcement.getUpdatedAt())
      .build();
  }
  
  private static String getFileUrl(Announcement announcement) {
    
    return Optional.ofNullable(announcement.getFile())
      .map(File::getFileUrl)
      .orElse(null);
  }
  
  public static PagingResponse<AnnouncementWebResponse> toAnnouncementsPagingResponse(
    Page<Announcement> data
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toAnnouncementWebResponseList(data),
                                           PageHelper.toPaging(data)
    );
  }
  
  private static List<AnnouncementWebResponse> toAnnouncementWebResponseList(
    Page<Announcement> data
  ) {
    
    return data.getContent()
      .stream()
      .map(AnnouncementResponseMapper::buildAnnouncementWebResponse)
      .collect(Collectors.toList());
  }
  
}
