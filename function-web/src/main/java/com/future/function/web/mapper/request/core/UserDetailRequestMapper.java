package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import com.future.function.web.model.request.core.ChangeProfilePictureWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public UserDetailRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public Pair<String, String> toOldAndNewPasswordPair(
    ChangePasswordWebRequest request
  ) {

    validator.validate(request);

    return Pair.of(request.getOldPassword(), request.getNewPassword());
  }

  public User toUser(ChangeProfilePictureWebRequest request, String email) {

    validator.validate(request);

    return User.builder()
      .email(email)
      .pictureV2(getFileV2(request))
      .build();
  }

  private FileV2 getFileV2(ChangeProfilePictureWebRequest request) {

    return Optional.of(request)
      .map(ChangeProfilePictureWebRequest::getAvatar)
      .filter(avatarSingleList -> !avatarSingleList.isEmpty())
      .map(list -> list.get(0))
      .map(this::buildFileV2)
      .orElse(null);
  }

  private FileV2 buildFileV2(String fileId) {

    return FileV2.builder()
      .id(fileId)
      .build();
  }

}
