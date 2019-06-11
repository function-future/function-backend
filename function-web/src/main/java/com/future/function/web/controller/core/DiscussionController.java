package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.DiscussionService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.core.DiscussionRequestMapper;
import com.future.function.web.mapper.response.core.DiscussionResponseMapper;
import com.future.function.web.model.request.core.DiscussionWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.DiscussionWebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/core/batches/{batchCode}/courses/{courseId" +
                        "}/discussions")
public class DiscussionController {
  
  private final DiscussionService discussionService;
  
  private final DiscussionRequestMapper discussionRequestMapper;
  
  public DiscussionController(
    DiscussionService discussionService,
    DiscussionRequestMapper discussionRequestMapper
  ) {
    
    this.discussionService = discussionService;
    this.discussionRequestMapper = discussionRequestMapper;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<DiscussionWebResponse> getDiscussions(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @PathVariable
      String batchCode,
    @PathVariable
      String courseId,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "4")
      int size
  ) {
    
    return DiscussionResponseMapper.toDiscussionPagingResponse(
      discussionService.getDiscussions(session.getEmail(), courseId, batchCode,
                                       PageHelper.toPageable(page, size)
      ));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<DiscussionWebResponse> createDiscussion(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @PathVariable
      String batchCode,
    @PathVariable
      String courseId,
    @RequestBody
      DiscussionWebRequest request
  ) {
    
    return DiscussionResponseMapper.toDiscussionDataResponse(
      discussionService.createDiscussion(
        discussionRequestMapper.toDiscussion(request, session.getEmail(),
                                             courseId, batchCode
        )));
  }
  
}
