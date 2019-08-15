package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.QuestionBankWebRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QuestionBankRequestMapper {

  private RequestValidator validator;

  @Autowired
  public QuestionBankRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public QuestionBank toQuestionBank(QuestionBankWebRequest request) {

    return toValidatedQuestionBank(request);
  }

  private QuestionBank toValidatedQuestionBank(QuestionBankWebRequest request) {

    return Optional.ofNullable(request)
      .map(validator::validate)
      .map(val -> {
        QuestionBank questionBank = new QuestionBank();
        BeanUtils.copyProperties(request, questionBank);
        return questionBank;
      })
      .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  public QuestionBank toQuestionBank(
    String id, QuestionBankWebRequest request
  ) {

    QuestionBank response = toValidatedQuestionBank(request);
    response.setId(id);
    return response;
  }

}
