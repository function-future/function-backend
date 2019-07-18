package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.repository.feature.scoring.OptionRepository;
import com.future.function.service.api.feature.scoring.OptionService;
import com.future.function.service.impl.helper.CopyHelper;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionServiceImpl implements OptionService {

  private OptionRepository optionRepository;

  @Autowired
  public OptionServiceImpl(OptionRepository optionRepository) {
    this.optionRepository = optionRepository;
  }

  @Override
  public List<Option> getOptionListByQuestionId(String questionId) {
    return Optional.ofNullable(questionId)
        .map(optionRepository::findAllByQuestionId)
        .orElseGet(ArrayList::new);
  }

  @Override
  public Option findById(String id) {
    return Optional.ofNullable(id)
        .flatMap(optionRepository::findByIdAndDeletedFalse)
        .orElseThrow(() -> new NotFoundException("Failed at #findById #OptionService"));
  }

  @Override
  public Option createOption(Option option) {
    return optionRepository.save(option);
  }

  @Override
  public Option updateOption(Option option) {
    return Optional.of(option)
        .map(Option::getId)
        .flatMap(optionRepository::findByIdAndDeletedFalse)
        .map(oldOption -> copyOptionRequestAttributes(option, oldOption))
        .map(optionRepository::save)
        .orElse(option);
  }

  private Option copyOptionRequestAttributes(Option option, Option oldOption) {
    CopyHelper.copyProperties(option, oldOption);
    return oldOption;
  }

  @Override
  public boolean isOptionCorrect(Option option) {
    return Optional.of(option)
        .map(Option::getId)
        .map(this::findById)
        .map(optionDB -> determineCorrectOption(option, optionDB))
        .orElse(false);
  }

  private boolean determineCorrectOption(Option option, Option optionDB) {
    return option.isCorrect() == optionDB.isCorrect();
  }

  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
        .flatMap(optionRepository::findByIdAndDeletedFalse)
        .ifPresent(this::setDeletedAndSaveOption);
  }

  private void setDeletedAndSaveOption(Option option) {
    option.setDeleted(true);
    optionRepository.save(option);
  }
}
