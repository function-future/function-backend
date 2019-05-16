package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.AnnouncementWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AnnouncementRequestMapper {
  
  private final ObjectValidator validator;
  
  private final WebRequestMapper requestMapper;
  
  @Autowired
  public AnnouncementRequestMapper(
    ObjectValidator validator, WebRequestMapper requestMapper
  ) {
    
    this.validator = validator;
    this.requestMapper = requestMapper;
  }
  
  /**
   * Converts JSON data to {@code Announcement} object.
   *
   * @param data JSON data (in form of String) to be converted.
   *
   * @return {@code Announcement} - Converted announcement object.
   */
  public Announcement toAnnouncement(String data) {
    
    AnnouncementWebRequest request = requestMapper.toWebRequestObject(
      data, AnnouncementWebRequest.class);
    
    return toValidatedAnnouncement(null, request);
  }
  
  private Announcement toValidatedAnnouncement(
    String announcementId, AnnouncementWebRequest request
  ) {
    
    validator.validate(request);
    
    Announcement announcement = Announcement.builder()
      .title(request.getTitle())
      .summary(request.getSummary())
      .descriptionHtml(request.getDescription())
      .fileV2s(toFileV2List(request))
      .build();
    
    if (announcementId != null) {
      announcement.setId(announcementId);
    }
    
    return announcement;
  }
  
  private List<FileV2> toFileV2List(AnnouncementWebRequest request) {
    
    return Optional.of(request)
      .map(AnnouncementWebRequest::getFiles)
      .map(List::stream)
      .map(stream -> stream.map(id -> FileV2.builder()
        .id(id)
        .build())
        .collect(Collectors.toList()))
      .orElseGet(Collections::emptyList);
  }
  
  public Announcement toAnnouncement(AnnouncementWebRequest request) {
    
    return toValidatedAnnouncement(null, request);
  }
  
  /**
   * Converts JSON data to {@code Announcement} object. This method is used for
   * update announcement purposes.
   *
   * @param announcementId Id of announcement to be updated.
   * @param data           JSON data (in form of String) to be converted.
   *
   * @return {@code Announcement} - Converted announcement object.
   */
  public Announcement toAnnouncement(String announcementId, String data) {
    
    return toValidatedAnnouncement(announcementId,
                                   requestMapper.toWebRequestObject(data,
                                                                    AnnouncementWebRequest.class
                                   )
    );
  }
  
  public Announcement toAnnouncement(
    String announcementId, AnnouncementWebRequest request
  ) {
    
    return toValidatedAnnouncement(announcementId, request);
  }
  
}
