package com.future.function.web.mapper.request.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.logging.LogMessageWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogMessageRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public LogMessageRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public LogMessage toLogMessage(LogMessageWebRequest logMessageWebRequest, String memberId, String topicId){
    return toValidatedLogMessage(logMessageWebRequest, memberId, topicId);
  }

  private LogMessage toValidatedLogMessage(LogMessageWebRequest logMessageWebRequest, String memberId, String topicId) {
    validator.validate(logMessageWebRequest);

    return LogMessage.builder()
            .text(logMessageWebRequest.getText())
            .sender(User.builder().id(memberId).build())
            .topic(Topic.builder().id(topicId).build())
            .build();
  }
}
