package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentQuizService {

  List<StudentQuiz> findAllByStudentId(String studentId);

  Page<StudentQuizDetail> findAllQuizByStudentId(String studentId, Pageable pageable);

  StudentQuiz findOrCreateByStudentIdAndQuizId(String studentId, String quizId);

  List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(
    String studentId, String quizId
  );

  StudentQuizDetail answerQuestionsByStudentQuizId(
    String studentId, String quizId, List<StudentQuestion> answers
  );

  void deleteById(String id);

  void deleteByBatchCodeAndQuiz(Quiz quiz);

}
