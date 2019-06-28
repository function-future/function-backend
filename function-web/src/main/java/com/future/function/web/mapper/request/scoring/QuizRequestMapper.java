package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.CopyQuizWebRequest;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Bean class used to map web request for Quiz entity
 */
@Slf4j
@Component
public class QuizRequestMapper {

  private RequestValidator validator;

  @Autowired
  public QuizRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  /**
   * Used to map web request to quiz entity object with quiz id
   *
   * @param id      (String)
   * @param request (QuizWebRequest)
   * @return Quiz object
   */
  public Quiz toQuiz(String id, QuizWebRequest request) {
    Quiz quiz = Optional.ofNullable(id)
        .filter(val -> !val.isEmpty())
        .map(val -> Quiz
            .builder()
            .id(val)
            .build())
        .orElseThrow(() -> new BadRequestException("Bad Request"));
    return toValidatedQuiz(request, quiz);
  }

  public CopyQuizWebRequest validateCopyQuizWebRequest(CopyQuizWebRequest request) {
    return validator.validate(request);
  }

  /**
   * Used to map web request to quiz entity object
   *
   * @param request (QuizWebRequest)
   * @return Quiz object
   */
  public Quiz toQuiz(QuizWebRequest request) {
    return toValidatedQuiz(request, new Quiz());
  }

  /**
   * Private method used to validate web request and map to quiz entity object
   *
   * @param request (QuizWebRequest)
   * @param quiz    (Quiz)
   * @return Quiz object
   */
  private Quiz toValidatedQuiz(QuizWebRequest request, Quiz quiz) {
    return Optional.ofNullable(request)
        .map(validator::validate)
        .map(val -> {
          BeanUtils.copyProperties(val, quiz);
          Batch batch = Batch.builder().code(val.getBatchCode()).build();
          quiz.setBatch(batch);
          quiz.setQuestionBanks(buildQuestionBanks(val.getQuestionBanks()));
          return quiz;
        })
        .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private List<QuestionBank> buildQuestionBanks(List<String> questionBankIds) {
    return questionBankIds
        .stream()
        .map(id -> QuestionBank.builder().id(id).build())
        .collect(Collectors.toList());
  }
}
