package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentQuizService {

    Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable);

    StudentQuiz findById(String id);

    StudentQuiz createStudentQuiz(String userId, Quiz quiz);

    Quiz createStudentQuizByBatchCode(String batchCode, Quiz quiz);

    Quiz copyQuizFromBatch(String targetBatch, Quiz quiz);

    void deleteById(String id);

    void deleteByBatchCodeAndQuiz(String batchCode, String quizId);
}
