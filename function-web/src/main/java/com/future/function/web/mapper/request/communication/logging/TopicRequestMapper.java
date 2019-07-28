package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.TopicRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author : Ricky Kennedy
 * Created At : 10:49 28/07/2019
 */
@Slf4j
@Component
public class TopicRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public TopicRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public Topic toTopic(TopicRequest topicRequest,String loggingRoomId, String topicId) {
    return toValidateTopic(topicRequest, loggingRoomId, topicId);
  }

  private Topic toValidateTopic(TopicRequest topicRequest, String loggingRoomId, String topicId) {
    validator.validate(topicRequest);

     Topic topic = Topic.builder()
            .title(topicRequest.getTitle())
            .loggingRoom(LoggingRoom.builder().id(loggingRoomId).build())
            .build();

      if(topicId != null) {
        topic.setId(topicId);
      }

      return topic;
  }

}
