package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.AuthWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthResponseMapper {

  public static DataResponse<AuthWebResponse> toAuthDataResponse(
    User user, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(HttpStatus.OK,
                                         AuthResponseMapper.buildAuthWebResponse(
                                           user, urlPrefix)
    );
  }

  private static AuthWebResponse buildAuthWebResponse(
    User user, String urlPrefix
  ) {

    return AuthWebResponse.builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .role(user.getRole()
              .name())
      .avatar(
        AuthResponseMapper.getThumbnailUrl(user.getPictureV2(), urlPrefix))
      .build();
  }

  private static String getThumbnailUrl(FileV2 file, String urlPrefix) {

    return Optional.ofNullable(file)
      .map(FileV2::getThumbnailUrl)
      .map(urlPrefix::concat)
      .orElseGet(() -> AuthResponseMapper.getFileUrl(file, urlPrefix));
  }

  private static String getFileUrl(FileV2 file, String urlPrefix) {

    return Optional.ofNullable(file)
      .map(FileV2::getFileUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }

}
