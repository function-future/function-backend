package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.StudentQuestionWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentQuestionRequestMapper {

  private RequestValidator validator;

  private OptionRequestMapper optionRequestMapper;

  @Autowired
  public StudentQuestionRequestMapper(RequestValidator validator, OptionRequestMapper optionRequestMapper) {
    this.validator = validator;
    this.optionRequestMapper = optionRequestMapper;
  }

  public StudentQuestion toStudentQuestion(StudentQuestionWebRequest request) {
    return toValidatedStudentQuestion(request);
  }

  private StudentQuestion toValidatedStudentQuestion(StudentQuestionWebRequest request) {
    request = validator.validate(request);

    return StudentQuestion
        .builder()
        .number(request.getNumber())
        .option(optionRequestMapper.toOptionFromOptionId(request.getOptionId()))
        .build();
  }

  public List<StudentQuestion> toStudentQuestionList(List<StudentQuestionWebRequest> requests) {
    return requests
        .stream()
        .map(this::toStudentQuestion)
        .collect(Collectors.toList());
  }

}
