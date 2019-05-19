package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentQuizService {

    Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable);

    StudentQuiz findById(String id);

    StudentQuiz createStudentQuiz(String userId, Quiz quiz);

    StudentQuiz createStudentQuizByBatchCode(Integer batchCode, Quiz quiz);

    StudentQuiz copyQuizFromBatch(Integer originBatch, Integer targetBatch, String studentQuizId);

    void deleteById(String id);

}
