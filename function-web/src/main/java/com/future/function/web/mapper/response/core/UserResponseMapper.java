package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper class for user web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseMapper {
  
  /**
   * Converts a user data to {@code UserWebResponse}, wrapped in {@code
   * DataResponse}.
   *
   * @param user User data to be converted to response.
   *
   * @return {@code DataResponse<UserWebResponse} - The converted user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  public static DataResponse<UserWebResponse> toUserDataResponse(User user) {
    
    return toUserDataResponse(HttpStatus.OK, user);
  }
  
  /**
   * Converts a user data to {@code UserWebResponse} given {@code HttpStatus},
   * wrapped in {@code DataResponse}.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param user       User data to be converted to response.
   *
   * @return {@code DataResponse<UserWebResponse} - The converted user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  public static DataResponse<UserWebResponse> toUserDataResponse(
    HttpStatus httpStatus, User user
  ) {
    
    return DataResponse.<UserWebResponse>builder().code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(buildUserWebResponse(user))
      .build();
  }
  
  private static UserWebResponse buildUserWebResponse(User user) {
    
    return UserWebResponse.builder()
      .role(user.getRoleAsString())
      .email(user.getEmail())
      .name(user.getName())
      .phone(user.getPhone())
      .address(user.getAddress())
      .deleted(user.isDeleted())
      .pictureUrl(user.getPicture()
                    .getFileUrl())
      .thumbnailUrl(user.getPicture()
                      .getThumbnailUrl())
      .batch(Optional.of(user)
               .map(User::getBatch)
               .map(Batch::getNumber)
               .orElse(null))
      .university(user.getUniversity())
      .build();
  }
  
  /**
   * Converts users data to {@code UserWebResponse} given {@code HttpStatus},
   * wrapped in {@code DataResponse}.
   *
   * @param data Users data to be converted to response.
   *
   * @return {@code PagingResponse<UserWebResponse} - The converted user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  public static PagingResponse<UserWebResponse> toUsersPagingResponse(
    Page<User> data
  ) {
    
    return PagingResponse.<UserWebResponse>builder().code(HttpStatus.OK.value())
      .status(HttpStatus.OK.getReasonPhrase())
      .data(data.getContent()
              .stream()
              .map(UserResponseMapper::buildUserWebResponse)
              .collect(Collectors.toList()))
      .paging(PageHelper.toPaging(data))
      .build();
  }
  
}
