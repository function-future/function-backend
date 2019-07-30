package com.future.function.service.api.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {

    Page<Topic> getTopicByLoggingRoom (String loggingRoomId, Pageable pageable);

    Topic getTopic (String topicId);

    Topic createTopic (Topic topic);

    Topic updateTopic (Topic topic);

    void deleteTopic (String topicId);

}
