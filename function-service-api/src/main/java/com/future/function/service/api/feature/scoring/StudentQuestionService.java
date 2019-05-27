package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;

import java.util.List;

public interface StudentQuestionService {

    List<StudentQuestion> findAllByStudentQuizDetailId(String studentQuizDetailId);

    Integer postAnswerForAllStudentQuestion(List<StudentQuestion> answers);

    List<StudentQuestion> createStudentQuestionsByStudentQuizDetail(StudentQuizDetail studentQuizDetail,
                                                                    List<StudentQuestion> studentQuestions);

    void deleteAllByStudentQuizDetailId(String studentQuizDetailId);

}
