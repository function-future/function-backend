package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuizDetailRepository extends MongoRepository<StudentQuizDetail, String> {

  Optional<StudentQuizDetail> findByIdAndDeletedFalse(String id);

  Optional<StudentQuizDetail> findFirstByStudentQuizIdAndDeletedFalse(String studentQuizId);

}
