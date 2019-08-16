package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionBankService {

  Page<QuestionBank> findAllByPageable(Pageable pageable);

  List<QuestionBank> findAll();

  QuestionBank findById(String id);

  QuestionBank createQuestionBank(QuestionBank request);

  QuestionBank updateQuestionBank(QuestionBank request);

  void deleteById(String id);

}
