package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.OptionWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OptionRequestMapper {

  private RequestValidator validator;

  @Autowired
  public OptionRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public Option toOption(OptionWebRequest request) {
    return Optional.ofNullable(request)
        .map(this::toValidatedOption)
        .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  public Option toOptionFromOptionId(String optionId) {
    return Optional.ofNullable(optionId)
        .filter(id -> !id.equals(""))
        .map(id -> Option.builder().id(id).build())
        .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private Option toValidatedOption(OptionWebRequest request) {
    request = validator.validate(request);
    return Option.builder()
        .id(request.getId())
        .label(request.getLabel())
        .correct(request.getCorrect() != null && request.getCorrect().equals(true))
        .build();
  }

}
