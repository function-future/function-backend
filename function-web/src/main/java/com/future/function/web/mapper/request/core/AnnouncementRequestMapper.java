package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.AnnouncementWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class AnnouncementRequestMapper {
  
  private final ObjectValidator validator;
  
  private final WebRequestMapper<AnnouncementWebRequest> requestMapper;
  
  @Autowired
  public AnnouncementRequestMapper(
    ObjectValidator validator,
    WebRequestMapper<AnnouncementWebRequest> requestMapper
  ) {
    
    this.validator = validator;
    this.requestMapper = requestMapper;
    requestMapper.setType(AnnouncementWebRequest.class);
  }
  
  public Announcement toAnnouncement(String data) {
    
    AnnouncementWebRequest request = requestMapper.toWebRequestObject(data);
    
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
  
  public Announcement toAnnouncement(String announcementId, String data) {
    
    return toValidatedAnnouncement(
      announcementId, requestMapper.toWebRequestObject(data));
  }
  
}
