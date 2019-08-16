package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.DiscussionWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscussionRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public DiscussionRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public Discussion toDiscussion(
    DiscussionWebRequest request, String email, String courseId,
    String batchCode
  ) {

    return toValidatedDiscussion(request, email, courseId, batchCode);
  }

  private Discussion toValidatedDiscussion(
    DiscussionWebRequest request, String email, String courseId,
    String batchCode
  ) {

    validator.validate(request);

    return Discussion.builder()
      .courseId(courseId)
      .batchCode(batchCode)
      .user(this.buildUser(email))
      .description(request.getComment())
      .build();
  }

  private User buildUser(String email) {

    return User.builder()
      .email(email)
      .build();
  }

}
