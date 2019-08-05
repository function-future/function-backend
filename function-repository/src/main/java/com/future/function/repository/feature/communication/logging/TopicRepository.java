package com.future.function.repository.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {

    Page<Topic> findAllByLoggingRoomAndDeletedFalse (LoggingRoom loggingRoom, Pageable pageable);
}
