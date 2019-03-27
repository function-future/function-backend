package com.future.function.web.mapper.response;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.user.User;
import com.future.function.web.mapper.PageHelper;
import com.future.function.web.model.base.DataResponse;
import com.future.function.web.model.base.PagingResponse;
import com.future.function.web.model.response.user.UserWebResponse;

public class UserResponseMapper {

  private static UserWebResponse buildUserWebResponse(User user) {

    return UserWebResponse.builder()
        .role(user.getRoleAsString())
        .email(user.getEmail())
        .name(user.getName())
        .phone(user.getPhone())
        .address(user.getAddress())
//        .pictureUrl(user.getPicture()
//            .getFileUrl())
        .batch(Optional.of(user)
            .map(User::getBatch)
            .orElse(Batch.builder().build())
            .getNumber())
        .university(user.getUniversity())
        .build();
  }

  public static DataResponse<UserWebResponse> toUserDataResponse(User user) {

    return DataResponse.<UserWebResponse>builder().code(HttpStatus.OK.value())
        .status(HttpStatus.OK.getReasonPhrase())
        .data(buildUserWebResponse(user))
        .build();
  }

  public static PagingResponse<UserWebResponse> toUsersPagingResponse(Page<User> data) {

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
