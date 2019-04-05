package com.future.function.web.mapper.response;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.model.entity.feature.user.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.user.UserWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseMapper {
  
  public static DataResponse<UserWebResponse> toUserDataResponse(User user) {
    
    return toUserDataResponse(HttpStatus.OK, user);
  }
  
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
