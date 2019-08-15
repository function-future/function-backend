package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentQuestionRepository
  extends MongoRepository<StudentQuestion, String> {

  List<StudentQuestion> findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(
    String studentQuizDetailId
  );

}
