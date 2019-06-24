package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentQuizService {

  Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable);

  StudentQuiz findById(String id);

  List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId);

  List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(String studentQuizId);

  StudentQuizDetail answerQuestionsByStudentQuizId(String studentQuizId, List<StudentQuestion> answers);

  StudentQuiz createStudentQuizAndSave(String userId, Quiz quiz);

  Quiz createStudentQuizByBatchCode(String batchCode, Quiz quiz);

  Quiz copyQuizWithTargetBatch(Batch targetBatch, Quiz quiz);

  void deleteById(String id);

  void deleteByBatchCodeAndQuiz(String batchCode, String quizId);
}
