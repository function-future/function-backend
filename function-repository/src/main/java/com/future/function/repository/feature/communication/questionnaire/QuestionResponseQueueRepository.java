package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseQueue;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Author : Ricky Kennedy
 * Created At : 19:57 12/11/2019
 */
public interface QuestionResponseQueueRepository
  extends MongoRepository<QuestionResponseQueue, String> {
}
