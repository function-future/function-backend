package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface to represent Quiz Service API
 */
public interface QuizService {

  Quiz findById(String id);

  Page<Quiz> findAllByBatchCodeAndPageable(String batchCode, Pageable pageable);

  Quiz copyQuizWithTargetBatchCode(String targetBatchCode, Quiz quiz);

  Quiz createQuiz(Quiz request);

  Quiz updateQuiz(Quiz request);

  void deleteById(String id);

}
