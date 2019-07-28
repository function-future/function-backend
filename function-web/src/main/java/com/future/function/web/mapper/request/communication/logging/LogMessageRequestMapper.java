package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LogMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author : Ricky Kennedy
 * Created At : 10:37 28/07/2019
 */
@Slf4j
@Component
public class LogMessageRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public LogMessageRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public LogMessage toLogMessage(LogMessageRequest logMessageRequest, String memberId, String topicId){
    return toValidatedLogMessage(logMessageRequest, memberId, topicId);
  }

  private LogMessage toValidatedLogMessage(LogMessageRequest logMessageRequest, String memberId, String topicId) {
    validator.validate(logMessageRequest);

    return LogMessage.builder()
            .text(logMessageRequest.getText())
            .sender(User.builder().id(memberId).build())
            .topic(Topic.builder().id(topicId).build())
            .build();
  }
}
