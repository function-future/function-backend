package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;

import java.util.List;

public interface StudentQuizDetailService {

  StudentQuizDetail findLatestByStudentQuizId(String studentQuizId);

  List<StudentQuestion> findAllUnansweredQuestionsByStudentQuizId(
    StudentQuiz studentQuiz
  );

  StudentQuizDetail answerStudentQuiz(
    String studentQuizId, List<StudentQuestion> answers
  );

  void deleteByStudentQuiz(StudentQuiz studentQuiz);

}
