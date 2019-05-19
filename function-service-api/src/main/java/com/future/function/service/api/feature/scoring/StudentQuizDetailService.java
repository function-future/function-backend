package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;

import java.util.List;

public interface StudentQuizDetailService {

    StudentQuizDetail findLatestByStudentQuizId(String studentQuizId);

    StudentQuizDetail answerStudentQuiz(String studentQuizId, List<Question> questionList);

}
