package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionBankService {

  Page<QuestionBank> findAllByPageable(Pageable pageable);

  QuestionBank findById(String id);

  QuestionBank createQuestionBank(QuestionBank request);

  QuestionBank updateQuestionBank(QuestionBank request);

  void deleteById(String id);

}
