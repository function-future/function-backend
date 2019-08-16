package com.future.function.repository.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogMessageRepository
  extends MongoRepository<LogMessage, String> {

  Page<LogMessage> findAllByTopicOrderByCreatedAtDesc(
    Topic topic, Pageable pageable
  );

}
