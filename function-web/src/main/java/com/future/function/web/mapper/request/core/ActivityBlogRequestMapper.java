package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.ActivityBlogWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ActivityBlogRequestMapper {
  
  private final RequestValidator validator;
  
  @Autowired
  public ActivityBlogRequestMapper(RequestValidator validator) {
    
    this.validator = validator;
  }
  
  public ActivityBlog toActivityBlog(
    String email, ActivityBlogWebRequest request
  ) {
    
    return this.toActivityBlog(email, null, request);
  }
  
  public ActivityBlog toActivityBlog(
    String email, String activityBlogId, ActivityBlogWebRequest request
  ) {
    
    return this.toValidatedActivityBlog(email, activityBlogId, request);
  }
  
  private ActivityBlog toValidatedActivityBlog(
    String email, String activityBlogId, ActivityBlogWebRequest request
  ) {
    
    validator.validate(request);
    
    ActivityBlog activityBlog = ActivityBlog.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .files(this.getFileV2s(request))
      .user(this.buildUser(email))
      .build();
    
    if (activityBlogId != null) {
      activityBlog.setId(activityBlogId);
    }
    
    return activityBlog;
  }
  
  private List<FileV2> getFileV2s(ActivityBlogWebRequest request) {
    
    return Optional.of(request)
      .map(ActivityBlogWebRequest::getFiles)
      .orElseGet(Collections::emptyList)
      .stream()
      .map(this::buildFileV2)
      .collect(Collectors.toList());
  }
  
  private User buildUser(String email) {
    
    return User.builder()
      .email(email)
      .build();
  }
  
  private FileV2 buildFileV2(String id) {
    
    return FileV2.builder()
      .id(id)
      .build();
  }
  
}
