package com.future.function.service.api.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogMessageService {

  Page<LogMessage> getLogMessagesByTopic(String topicId, Pageable pageable);

  LogMessage createLogMessage(LogMessage logMessage);

}
