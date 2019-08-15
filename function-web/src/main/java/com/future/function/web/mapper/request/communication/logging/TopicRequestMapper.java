package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.TopicWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public TopicRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public Topic toTopic(
    TopicWebRequest topicWebRequest, String loggingRoomId, String topicId
  ) {

    return toValidateTopic(topicWebRequest, loggingRoomId, topicId);
  }

  private Topic toValidateTopic(
    TopicWebRequest topicWebRequest, String loggingRoomId, String topicId
  ) {

    validator.validate(topicWebRequest);

    Topic topic = Topic.builder()
      .title(topicWebRequest.getTitle())
      .loggingRoom(LoggingRoom.builder()
                     .id(loggingRoomId)
                     .build())
      .build();

    if (topicId != null) {
      topic.setId(topicId);
    }

    return topic;
  }

}
