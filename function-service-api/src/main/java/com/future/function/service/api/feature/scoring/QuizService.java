package com.future.function.service.api.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.session.model.Session;
import java.util.Observable;
import java.util.Observer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuizService {

  Quiz findById(String id, Role role, String sessionBatchId);

  Page<Quiz> findAllByBatchCodeAndPageable(String batchCode, Pageable pageable, Role role, String sessionBatchId, boolean deadline);

  Quiz copyQuizWithTargetBatchCode(String targetBatchCode, String quizId);

  Quiz createQuiz(Quiz request);

  Quiz updateQuiz(Quiz request);

  void deleteById(String id);

  void addObserver(Observer observer);

}
