package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.util.constant.FieldName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionBankService {

  Page<QuestionBank> findAllByPageableFilterAndSearch(Pageable pageable, String filter, String search);

  QuestionBank findById(String id);

  QuestionBank createQuestionBank(QuestionBank request);

  QuestionBank updateQuestionBank(QuestionBank request);

  void deleteById(String id);

}
