package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;

import java.util.List;

public interface StudentQuestionService {

  List<StudentQuestion> findAllByStudentQuizDetailId(
    String studentQuizDetailId
  );

  List<Question> findAllRandomQuestionsFromMultipleQuestionBank(
    List<QuestionBank> questionBanks, int questionCount
  );

  List<StudentQuestion> createStudentQuestionsFromQuestionList(
    List<Question> questionList, StudentQuizDetail studentQuizDetail
  );

  Integer postAnswerForAllStudentQuestion(
    List<StudentQuestion> answers, String studentQuizDetailId
  );

  List<StudentQuestion> createStudentQuestionsByStudentQuizDetail(
    StudentQuizDetail studentQuizDetail, List<StudentQuestion> studentQuestions
  );

  void deleteAllByStudentQuizDetailId(String studentQuizDetailId);

}
