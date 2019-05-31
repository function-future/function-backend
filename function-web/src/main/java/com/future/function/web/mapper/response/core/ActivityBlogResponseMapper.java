package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.embedded.AuthorWebResponseMapper;
import com.future.function.web.mapper.response.core.embedded.EmbeddedFileWebResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.ActivityBlogWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for announcement web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActivityBlogResponseMapper {
  
  public static PagingResponse<ActivityBlogWebResponse> toActivityBlogPagingResponse(
    Page<ActivityBlog> activityBlogs
  ) {
    
    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           ActivityBlogResponseMapper.toActivityBlogWebResponseList(
                                             activityBlogs),
                                           PageHelper.toPaging(activityBlogs)
    );
  }
  
  private static List<ActivityBlogWebResponse> toActivityBlogWebResponseList(
    Page<ActivityBlog> activityBlogs
  ) {
    
    return activityBlogs.getContent()
      .stream()
      .map(ActivityBlogResponseMapper::buildActivityBlogWebResponse)
      .collect(Collectors.toList());
  }
  
  public static DataResponse<ActivityBlogWebResponse> toActivityBlogDataResponse(
    ActivityBlog activityBlog
  ) {
    
    return ActivityBlogResponseMapper.toActivityBlogDataResponse(
      HttpStatus.OK, activityBlog);
  }
  
  public static DataResponse<ActivityBlogWebResponse> toActivityBlogDataResponse(
    HttpStatus httpStatus, ActivityBlog activityBlog
  ) {
    
    return ResponseHelper.toDataResponse(httpStatus,
                                         ActivityBlogResponseMapper.buildActivityBlogWebResponse(
                                           activityBlog)
    );
  }
  
  private static ActivityBlogWebResponse buildActivityBlogWebResponse(
    ActivityBlog activityBlog
  ) {
    
    return ActivityBlogWebResponse.builder()
      .id(activityBlog.getId())
      .title(activityBlog.getTitle())
      .description(activityBlog.getDescription())
      .files(EmbeddedFileWebResponseMapper.toEmbeddedFileWebResponses(
        activityBlog.getFiles()))
      .author(
        AuthorWebResponseMapper.buildAuthorWebResponse(activityBlog.getUser()))
      .build();
  }
  
}
