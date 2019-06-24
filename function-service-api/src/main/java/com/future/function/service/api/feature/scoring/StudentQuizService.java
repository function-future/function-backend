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

  Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable, String userId);

  StudentQuiz findById(String id, String userId);

  List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId, String userId);

  List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(String studentQuizId, String userId);

  StudentQuizDetail answerQuestionsByStudentQuizId(String studentQuizId, String userId, List<StudentQuestion> answers);

  StudentQuiz createStudentQuizAndSave(String userId, Quiz quiz);

  Quiz createStudentQuizByBatchCode(String batchCode, Quiz quiz);

  Quiz copyQuizWithTargetBatch(Batch targetBatch, Quiz quiz);

  void deleteById(String id);

  void deleteByBatchCodeAndQuiz(String batchCode, String quizId);
}
