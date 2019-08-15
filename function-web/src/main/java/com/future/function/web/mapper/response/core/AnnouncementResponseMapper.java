package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.embedded.EmbeddedFileWebResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnouncementResponseMapper {

  public static DataResponse<AnnouncementWebResponse> toAnnouncementDataResponse(
    Announcement announcement, String urlPrefix
  ) {

    return toAnnouncementDataResponse(HttpStatus.OK, announcement, urlPrefix);
  }

  public static DataResponse<AnnouncementWebResponse> toAnnouncementDataResponse(
    HttpStatus httpStatus, Announcement announcement, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(httpStatus,
                                         buildAnnouncementWebResponse(
                                           announcement, urlPrefix)
    );
  }

  private static AnnouncementWebResponse buildAnnouncementWebResponse(
    Announcement announcement, String urlPrefix
  ) {

    return AnnouncementWebResponse.builder()
      .id(announcement.getId())
      .title(announcement.getTitle())
      .summary(announcement.getSummary())
      .description(announcement.getDescription())
      .files(EmbeddedFileWebResponseMapper.toEmbeddedFileWebResponses(
        announcement.getFileV2s(), urlPrefix))
      .updatedAt(announcement.getUpdatedAt())
      .build();
  }

  public static PagingResponse<AnnouncementWebResponse> toAnnouncementsPagingResponse(
    Page<Announcement> data, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toAnnouncementWebResponseList(data,
                                                                         urlPrefix
                                           ), PageHelper.toPaging(data)
    );
  }

  private static List<AnnouncementWebResponse> toAnnouncementWebResponseList(
    Page<Announcement> data, String urlPrefix
  ) {

    return data.getContent()
      .stream()
      .map(
        announcement -> AnnouncementResponseMapper.buildAnnouncementWebResponse(
          announcement, urlPrefix))
      .collect(Collectors.toList());
  }

}
