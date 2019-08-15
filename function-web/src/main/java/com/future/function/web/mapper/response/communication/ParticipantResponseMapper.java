package com.future.function.web.mapper.response.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import com.future.function.web.model.response.feature.embedded.ParticipantDetailResponse;

import java.util.Optional;

public class ParticipantResponseMapper {

  public static ParticipantDetailResponse toParticipantDetailResponse(
    User user, String urlPrefix
  ) {

    return ParticipantDetailResponse.builder()
      .id(user.getId())
      .avatar(getAvatarThumbnailUrl(user.getPictureV2(), urlPrefix))
      .batch(getBatch(user.getBatch()))
      .name(user.getName())
      .type(getRole(user))
      .university(user.getUniversity())
      .build();
  }

  private static String getRole(User user) {

    return Optional.ofNullable(user.getRole())
      .map(Role::name)
      .orElse(null);
  }

  public static String getAvatarThumbnailUrl(FileV2 fileV2, String urlPrefix) {

    return Optional.ofNullable(fileV2)
      .map(FileV2::getThumbnailUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }

  private static BatchWebResponse getBatch(Batch batch) {

    return Optional.ofNullable(batch)
      .map(BatchResponseMapper::toBatchDataResponse)
      .map(DataResponse::getData)
      .orElse(null);
  }

}
