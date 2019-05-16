package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.File;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper class for announcement web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnouncementResponseMapper {
  
  /**
   * Converts an announcement data to {@code AnnouncementWebResponse},
   * wrapped in {@code DataResponse}.
   *
   * @param announcement Announcement data to be converted to response.
   *
   * @return {@code DataResponse<AnnouncementWebResponse>} - The converted
   * announcement data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
  public static DataResponse<AnnouncementWebResponse> toAnnouncementDataResponse(
    Announcement announcement
  ) {
    
    return toAnnouncementDataResponse(HttpStatus.OK, announcement);
  }
  
  /**
   * Converts a announcement data to {@code AnnouncementWebResponse} given
   * {@code HttpStatus}, wrapped in {@code DataResponse}.
   *
   * @param httpStatus   Http status to be shown in the response.
   * @param announcement Announcement data to be converted to response.
   *
   * @return {@code DataResponse<AnnouncementWebResponse>} - The converted
   * announcement data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
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
      .id(announcement.getId())
      .title(announcement.getTitle())
      .summary(announcement.getSummary())
      .description(announcement.getDescriptionHtml())
      .files(getFiles(announcement))
      .updatedAt(announcement.getUpdatedAt())
      .build();
  }
  
  private static List<FileWebResponse> getFiles(Announcement announcement) {
    
    return Optional.of(announcement)
      .map(Announcement::getFileV2s)
      .map(List::stream)
      .map(stream -> stream.map(ResourceResponseMapper::buildFileWebResponse)
        .collect(Collectors.toList()))
      .orElseGet(Collections::emptyList);
  }
  
  private static String getFileUrl(Announcement announcement) {
    
    return Optional.ofNullable(announcement.getFile())
      .map(File::getFileUrl)
      .orElse(null);
  }
  
  /**
   * Converts announcements data to {@code AnnouncementWebResponse} given
   * {@code HttpStatus}, wrapped in {@code PagingResponse}.
   *
   * @param data Announcements data to be converted to response.
   *
   * @return {@code PagingResponse<AnnouncementWebResponse} - The converted
   * announcements data, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
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
