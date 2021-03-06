package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.QuestionWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QuestionRequestMapper {

  private RequestValidator validator;

  private OptionRequestMapper requestMapper;

  @Autowired
  public QuestionRequestMapper(
    RequestValidator validator, OptionRequestMapper requestMapper
  ) {

    this.validator = validator;
    this.requestMapper = requestMapper;
  }

  public Question toQuestion(QuestionWebRequest request, String questionId) {

    Question question = toQuestion(request);
    return Optional.ofNullable(questionId)
      .map(id -> {
        question.setId(id);
        return question;
      })
      .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  public Question toQuestion(QuestionWebRequest request) {

    return Optional.ofNullable(request)
      .map(this::toValidatedQuestion)
      .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private Question toValidatedQuestion(QuestionWebRequest request) {

    request = validator.validate(request);
    return Question.builder()
      .label(request.getLabel())
      .options(getAndMapOptionWebRequest(request))
      .build();
  }

  private List<Option> getAndMapOptionWebRequest(QuestionWebRequest request) {

    return request.getOptions()
      .stream()
      .map(requestMapper::toOption)
      .collect(Collectors.toList());
  }

}
