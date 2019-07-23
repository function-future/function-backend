package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentQuizRepository extends MongoRepository<StudentQuiz, String> {

  Optional<StudentQuiz> findByIdAndDeletedFalse(String id);

  Optional<StudentQuiz> findByStudentIdAndQuizIdAndDeletedFalse(String studentId, String quizId);

  Page<StudentQuiz> findAllByStudentIdAndDeletedFalse(String studentId, Pageable pageable);

  Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable);

    List<StudentQuiz> findAllByStudentIdAndDeletedFalse(String studentId);

}
