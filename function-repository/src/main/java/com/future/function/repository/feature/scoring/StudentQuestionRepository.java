package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentQuestionRepository extends MongoRepository<StudentQuestion, String> {

  List<StudentQuestion> findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(String studentQuizDetailId);

}
