package com.future.function.service.impl.feature.communication.logging;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.repository.feature.communication.logging.TopicRepository;
import com.future.function.service.api.feature.communication.logging.LoggingRoomService;
import com.future.function.service.api.feature.communication.logging.TopicService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final LoggingRoomService loggingRoomService;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, LoggingRoomService loggingRoomService) {
        this.topicRepository = topicRepository;
        this.loggingRoomService = loggingRoomService;
    }

    @Override
    public Page<Topic> getTopicByLoggingRoom(String loggingRoomId, Pageable pageable) {
        return topicRepository.findAllByLoggingRoomAndDeletedFalse(
                loggingRoomService.getLoggingRoom(loggingRoomId),
                pageable
                );
    }

    @Override
    public Topic getTopic(String topicId) {
        return Optional.of(topicId)
                .map(topicRepository::findOne)
                .orElseThrow(() -> new NotFoundException("Topic Not found"));
    }

    @Override
    public Topic createTopic(Topic topic) {
        return Optional.of(topic)
                .map(this::setLoggingRoom)
                .map(topicRepository::save)
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Topic setLoggingRoom(Topic topic) {
        topic.setLoggingRoom(
          loggingRoomService.getLoggingRoom(topic.getLoggingRoom().getId()));
        return topic;
    }

    @Override
    public Topic updateTopic(Topic topic) {
        return Optional.of(topic)
                .map(Topic::getId)
                .map(topicRepository::findOne)
                .map(savedTopic -> this.copyProperties(savedTopic, topic))
                .map(topicRepository::save)
                .orElse(topic);
    }


    @Override
    public void deleteTopic(String topicId) {
        Optional.ofNullable(topicId)
          .map(topicRepository::findOne)
          .ifPresent(this::softDeletedHelper);
    }

    private void softDeletedHelper(Topic topic) {
        topic.setDeleted(true);
        topicRepository.save(topic);
    }

    private Topic copyProperties(Topic savedTopic, Topic topic) {
        savedTopic.setTitle(topic.getTitle());
        return savedTopic;
    }

}
