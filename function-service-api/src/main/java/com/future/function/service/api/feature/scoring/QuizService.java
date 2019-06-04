package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface to represent Quiz Service API
 */
public interface QuizService {

  Quiz findById(String id);

  Page<Quiz> findAllByPageableAndFilterAndSearch(Pageable pageable, String filter, String search);

  List<Question> findAllQuestionByMultipleQuestionBank(boolean random, String quizId);

  Quiz copyQuizWithTargetBatch(String targetBatch, Quiz quiz);

  Quiz createQuiz(Quiz request);

  Quiz updateQuiz(Quiz request);

  void deleteById(String id);

}
