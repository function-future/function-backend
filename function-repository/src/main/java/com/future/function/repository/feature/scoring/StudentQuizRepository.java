package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuiz;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuizRepository extends MongoRepository<StudentQuiz, String> {

  Optional<StudentQuiz> findByIdAndDeletedFalse(String id);

  Optional<StudentQuiz> findByStudentIdAndQuizIdAndDeletedFalse(String studentId, String quizId);

  Page<StudentQuiz> findAllByStudentIdAndDeletedFalse(String studentId, Pageable pageable);

}
