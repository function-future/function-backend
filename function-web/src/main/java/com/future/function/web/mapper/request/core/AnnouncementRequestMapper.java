package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.AnnouncementWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper class for incoming request for announcement feature.
 */
@Component
public class AnnouncementRequestMapper {
  
  private final RequestValidator validator;
  
  private final WebRequestMapper requestMapper;
  
  @Autowired
  public AnnouncementRequestMapper(
    RequestValidator validator, WebRequestMapper requestMapper
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
      .title(request.getAnnouncementTitle())
      .summary(request.getAnnouncementSummary())
      .descriptionHtml(request.getAnnouncementDescriptionHtml())
      .build();
    
    if (announcementId != null) {
      announcement.setId(announcementId);
    }
    
    return announcement;
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
  
}
