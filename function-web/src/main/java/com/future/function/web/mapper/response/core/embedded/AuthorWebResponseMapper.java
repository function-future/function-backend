package com.future.function.web.mapper.response.core.embedded;

import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.feature.core.embedded.AuthorWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorWebResponseMapper {
  
  public static AuthorWebResponse buildAuthorWebResponse(
    User user
  ) {
    
    return AuthorWebResponse.builder()
      .id(user.getId())
      .name(user.getName())
      .build();
  }
  
}
