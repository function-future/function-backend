package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.ActivityBlogService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.ActivityBlogRequestMapper;
import com.future.function.web.mapper.response.core.ActivityBlogResponseMapper;
import com.future.function.web.model.request.core.ActivityBlogWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.ActivityBlogWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for activity blog APIs.
 */
@RestController
@RequestMapping(value = "/api/core/activity-blogs")
public class ActivityBlogController {
  
  private final ActivityBlogService activityBlogService;
  
  private final ActivityBlogRequestMapper activityBlogRequestMapper;
  
  @Autowired
  public ActivityBlogController(
    ActivityBlogService activityBlogService,
    ActivityBlogRequestMapper activityBlogRequestMapper
  ) {
    
    this.activityBlogService = activityBlogService;
    this.activityBlogRequestMapper = activityBlogRequestMapper;
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
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{activityBlogId}")
  public DataResponse<ActivityBlogWebResponse> getActivityBlog(
    @PathVariable
      String activityBlogId
  ) {
    
    return ActivityBlogResponseMapper.toActivityBlogDataResponse(
      activityBlogService.getActivityBlog(activityBlogId));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<ActivityBlogWebResponse> createActivityBlog(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestBody
      ActivityBlogWebRequest request
  ) {
    
    return ActivityBlogResponseMapper.toActivityBlogDataResponse(
      HttpStatus.CREATED, activityBlogService.createActivityBlog(
        activityBlogRequestMapper.toActivityBlog(session.getEmail(), request)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{activityBlogId}")
  public DataResponse<ActivityBlogWebResponse> updateActivityBlog(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestBody
      ActivityBlogWebRequest request,
    @PathVariable
      String activityBlogId
  ) {
    
    return ActivityBlogResponseMapper.toActivityBlogDataResponse(
      activityBlogService.updateActivityBlog(
        activityBlogRequestMapper.toActivityBlog(session.getEmail(),
                                                 activityBlogId, request
        )));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{activityBlogId}")
  public BaseResponse deleteActivityBlog(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @PathVariable
      String activityBlogId
  ) {
    
    activityBlogService.deleteActivityBlog(session.getEmail(), activityBlogId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
