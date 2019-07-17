package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.repository.feature.scoring.QuestionBankRepository;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

  private QuestionBankRepository questionBankRepository;

  @Autowired
  public QuestionBankServiceImpl(QuestionBankRepository questionBankRepository) {
    this.questionBankRepository = questionBankRepository;
  }

  @Override
  public Page<QuestionBank> findAllByPageable(Pageable pageable) {
    return questionBankRepository.findAllByDeletedFalse(pageable);
  }

  @Override
  public QuestionBank findById(String id) {
    return Optional.ofNullable(id)
        .flatMap(questionBankRepository::findByIdAndDeletedFalse)
            .orElseThrow(() -> new NotFoundException("#Failed at #findById #QuestionBankService"));
  }

  @Override
  public QuestionBank createQuestionBank(QuestionBank questionBank) {
    return questionBankRepository.save(questionBank);
  }

  @Override
  public QuestionBank updateQuestionBank(QuestionBank questionBank) {
    return Optional.ofNullable(questionBank)
        .map(QuestionBank::getId)
        .flatMap(questionBankRepository::findByIdAndDeletedFalse)
        .map(foundQuestionBank -> mergeFoundAndNewQuestionBankThenSave(foundQuestionBank, questionBank))
        .orElse(questionBank);
  }

  private QuestionBank mergeFoundAndNewQuestionBankThenSave(QuestionBank foundQuestionBank, QuestionBank questionBank) {
    CopyHelper.copyProperties(questionBank, foundQuestionBank);
    return questionBankRepository.save(foundQuestionBank);
  }

  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
        .flatMap(questionBankRepository::findByIdAndDeletedFalse)
        .ifPresent(val -> {
          val.setDeleted(true);
          questionBankRepository.save(val);
        });
  }
}
