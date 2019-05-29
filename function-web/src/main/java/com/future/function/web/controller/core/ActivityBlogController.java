package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.ActivityBlogService;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.core.ActivityBlogResponseMapper;
import com.future.function.web.model.request.core.ActivityBlogWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.ActivityBlogWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Controller class for activity blog APIs.
 */
@RestController
@RequestMapping(value = "/api/core/activity-blogs")
public class ActivityBlogController {
  
  private final ActivityBlogService activityBlogService;
  
  @Autowired
  private RequestValidator validator;
  
  public ActivityBlogController(ActivityBlogService activityBlogService) {
    
    this.activityBlogService = activityBlogService;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<ActivityBlogWebResponse> getActivityBlogs(
    @RequestParam(defaultValue = "")
      String userId,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "5")
      int size,
    @RequestParam(defaultValue = "")
      String search
  ) {
    
    return ActivityBlogResponseMapper.toActivityBlogPagingResponse(
      activityBlogService.getActivityBlogs(userId, search,
                                           PageHelper.toPageable(page, size)
      ));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<ActivityBlogWebResponse> createActivityBlog(
    @RequestBody
      ActivityBlogWebRequest request,
    @RequestParam
      String email
  ) {
    
    validator.validate(request);
    
    ActivityBlog activityBlog = ActivityBlog.builder()
      .title(request.getTitle())
      .description(request.getDescription())
      .files(request.getFiles()
               .stream()
               .map(id -> FileV2.builder()
                 .id(id)
                 .build())
               .collect(Collectors.toList()))
      .user(User.builder()
              .email(email)
              .build())
      .build();
    
    return ActivityBlogResponseMapper.toActivityBlogDataResponse(
      HttpStatus.CREATED, activityBlogService.createActivityBlog(activityBlog));
  }
  
}
