package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Option;

import java.util.List;

public interface OptionService {

  List<Option> getOptionListByQuestionId(String questionId);

  Option findById(String id);

  Option createOption(Option option);

  Option updateOption(Option option);

  boolean isOptionCorrect(Option option);

  void deleteById(String id);

}
