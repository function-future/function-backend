package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.DiscussionWebResponse;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscussionResponseMapper {

  public static DataResponse<DiscussionWebResponse> toDiscussionDataResponse(
    Discussion discussion
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.CREATED, buildDiscussionWebResponse(discussion));
  }

  private static DiscussionWebResponse buildDiscussionWebResponse(
    Discussion discussion
  ) {

    return DiscussionWebResponse.builder()
      .id(discussion.getId())
      .comment(discussion.getDescription())
      .author(buildAuthorWebResponseObject(discussion))
      .createdAt(discussion.getCreatedAt())
      .build();
  }

  private static AuthorWebResponse buildAuthorWebResponseObject(
    Discussion discussion
  ) {

    User user = discussion.getUser();

    return AuthorWebResponse.builder()
      .id(user.getId())
      .name(user.getName())
      .build();
  }

  public static PagingResponse<DiscussionWebResponse> toDiscussionPagingResponse(
    Page<Discussion> data
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           DiscussionResponseMapper.toDiscussionWebResponseList(
                                             data.getContent()),
                                           PageHelper.toPaging(data)
    );
  }

  private static List<DiscussionWebResponse> toDiscussionWebResponseList(
    List<Discussion> data
  ) {

    return data.stream()
      .map(DiscussionResponseMapper::buildDiscussionWebResponse)
      .collect(Collectors.toList());
  }

}
