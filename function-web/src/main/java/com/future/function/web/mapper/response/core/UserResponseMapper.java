package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
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

import java.util.List;
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
   * @return {@code DataResponse<UserWebResponse>} - The converted user data,
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
   * @return {@code DataResponse<UserWebResponse>} - The converted user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  public static DataResponse<UserWebResponse> toUserDataResponse(
    HttpStatus httpStatus, User user
  ) {
    
    return ResponseHelper.toDataResponse(
      httpStatus, buildUserWebResponse(user));
  }
  
  private static UserWebResponse buildUserWebResponse(User user) {
    
    return UserWebResponse.builder()
      .id(user.getId())
      .role(user.getRole()
              .name())
      .email(user.getEmail())
      .name(user.getName())
      .phone(user.getPhone())
      .address(user.getAddress())
      .deleted(user.isDeleted())
      .avatar(UserResponseMapper.getFileUrl(user.getPictureV2()))
      .batch(BatchResponseMapper.toBatchWebResponse(user.getBatch()))
      .university(user.getUniversity())
      .build();
  }
  
  private static String getFileUrl(FileV2 fileV2) {
    
    return Optional.ofNullable(fileV2)
      .map(FileV2::getFileUrl)
      .orElse(null);
  }
  
  private static String getBatch(User user) {
    
    return Optional.of(user)
      .map(User::getBatch)
      .map(Batch::getCode)
      .orElse(null);
  }
  
  /**
   * Converts users data to {@code UserWebResponse} given {@code HttpStatus},
   * wrapped in {@code PagingResponse}.
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
    
    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, toUserWebResponseList(data), PageHelper.toPaging(data));
  }
  
  private static List<UserWebResponse> toUserWebResponseList(Page<User> data) {
    
    return data.getContent()
      .stream()
      .map(UserResponseMapper::buildUserWebResponse)
      .collect(Collectors.toList());
  }
  
}
