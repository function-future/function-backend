package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentQuizDetailRepository
  extends MongoRepository<StudentQuizDetail, String> {

  Optional<StudentQuizDetail> findByIdAndDeletedFalse(String id);

  Optional<StudentQuizDetail> findTopByStudentQuizIdAndDeletedFalseOrderByCreatedAtDesc(
    String studentQuizId
  );

  List<StudentQuizDetail> findAllByStudentQuizIdAndDeletedFalse(String studentQuizId);
}
