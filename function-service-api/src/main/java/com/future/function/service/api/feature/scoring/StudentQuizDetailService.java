package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;

import java.util.List;

public interface StudentQuizDetailService {

  StudentQuizDetail findLatestByStudentQuizId(String studentQuizId);

  List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId);

  List<StudentQuestion> findAllUnansweredQuestionsByStudentQuizId(
    String studentQuizId
  );

  StudentQuizDetail answerStudentQuiz(
    String studentQuizId, List<StudentQuestion> answers
  );

  StudentQuizDetail createStudentQuizDetail(
    StudentQuiz studentQuiz, List<StudentQuestion> questions
  );

  void deleteByStudentQuiz(StudentQuiz studentQuiz);

}
