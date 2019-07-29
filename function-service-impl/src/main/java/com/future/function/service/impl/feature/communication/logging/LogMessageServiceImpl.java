package com.future.function.service.impl.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.repository.feature.communication.logging.LogMessageRepository;
import com.future.function.service.api.feature.communication.logging.LogMessageService;
import com.future.function.service.api.feature.communication.logging.TopicService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogMessageServiceImpl implements LogMessageService {
  private final LogMessageRepository logMessageRepository;

  private final TopicService topicService;

  private final UserService userService;

  @Autowired
  public LogMessageServiceImpl(LogMessageRepository logMessageRepository, TopicService topicService, UserService userService) {
    this.logMessageRepository = logMessageRepository;
    this.topicService = topicService;
    this.userService = userService;
  }

  @Override
  public Page<LogMessage> getLogMessagesByTopic(String topicId, Pageable pageable) {
    return logMessageRepository.findAllByTopicOrderByCreatedAtDesc(
      topicService.getTopic(topicId),
      pageable
    );
  }

  @Override
  public LogMessage createLogMessage(LogMessage logMessage) {
    return Optional.of(logMessage)
            .map(this::setSender)
            .map(this::setTopic)
            .map(logMessageRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  private LogMessage setTopic(LogMessage logMessage) {
    logMessage.setTopic(topicService.getTopic(
      logMessage.getTopic().getId()));
    return logMessage;
  }

  private LogMessage setSender(LogMessage logMessage) {
    logMessage.setSender(userService.getUser(logMessage.getSender().getId()));
    return logMessage;
  }

}
