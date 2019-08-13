package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseMapper {

  public static DataResponse<UserWebResponse> toUserDataResponse(
    User user, String urlPrefix
  ) {

    return toUserDataResponse(HttpStatus.OK, user, urlPrefix);
  }

  public static DataResponse<UserWebResponse> toUserDataResponse(
    HttpStatus httpStatus, User user, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(
      httpStatus, buildUserWebResponse(user, urlPrefix));
  }

  private static UserWebResponse buildUserWebResponse(
    User user, String urlPrefix
  ) {

    return UserWebResponse.builder()
      .id(user.getId())
      .role(user.getRole()
              .name())
      .email(user.getEmail())
      .name(user.getName())
      .phone(user.getPhone())
      .address(user.getAddress())
      .avatar(UserResponseMapper.getFileUrl(user.getPictureV2(), urlPrefix))
      .avatarId(UserResponseMapper.getFileId(user))
      .batch(UserResponseMapper.getBatch(user))
      .university(user.getUniversity())
      .build();
  }

  private static String getFileId(User user) {

    return Optional.ofNullable(user.getPictureV2())
      .map(FileV2::getId)
      .orElse(null);
  }

  private static BatchWebResponse getBatch(User user) {

    return Optional.ofNullable(user.getBatch())
      .map(BatchResponseMapper::toBatchWebResponse)
      .orElse(null);
  }

  private static String getFileUrl(FileV2 fileV2, String urlPrefix) {

    return Optional.ofNullable(fileV2)
      .map(FileV2::getFileUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }

  public static PagingResponse<UserWebResponse> toUsersPagingResponse(
    Page<User> data, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK, toUserWebResponseList(
      data.getContent(), urlPrefix), PageHelper.toPaging(data));
  }

  public static List<UserWebResponse> toUserWebResponseList(
    List<User> data, String urlPrefix
  ) {

    return data.stream()
      .map(user -> buildUserWebResponse(user, urlPrefix))
      .collect(Collectors.toList());
  }

  public static PagingResponse<UserWebResponse> toUsersPagingResponseWithFinalPoint(
    Page<Pair<User, Integer>> pairPage, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, buildUserWebResponseAndSetFinalPoint(pairPage, urlPrefix),
      PageHelper.toPaging(pairPage)
    );
  }

  private static List<UserWebResponse> buildUserWebResponseAndSetFinalPoint(
    Page<Pair<User, Integer>> pairPage, String urlPrefix
  ) {

    return pairPage.getContent()
      .stream()
      .map(pair -> {
        UserWebResponse response = buildUserWebResponse(
          pair.getFirst(), urlPrefix);
        response.setFinalPoint(pair.getSecond());
        return response;
      })
      .collect(Collectors.toList());
  }

}
