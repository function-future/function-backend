package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.repository.feature.scoring.QuestionBankRepository;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

  private QuestionBankRepository questionBankRepository;

  @Autowired
  public QuestionBankServiceImpl(QuestionBankRepository questionBankRepository) {
    this.questionBankRepository = questionBankRepository;
  }

  @Override
  public Page<QuestionBank> findAllByPageableFilterAndSearch(Pageable pageable, String filter, String search) {
    return questionBankRepository.findAll(pageable);
  }

  @Override
  public QuestionBank findById(String id) {
    return Optional.ofNullable(id)
            .filter(val -> !val.isEmpty())
            .flatMap(questionBankRepository::findByIdAndDeletedFalse)
            .orElseThrow(() -> new NotFoundException("Question Bank Not Found"));
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
            .map(bankOld -> mergeOldAndNewQuestionBankThenSave(bankOld, questionBank))
            .orElse(questionBank);
  }

  private QuestionBank mergeOldAndNewQuestionBankThenSave(QuestionBank old, QuestionBank questionBank) {
    BeanUtils.copyProperties(questionBank, old);
    return questionBankRepository.save(old);
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
