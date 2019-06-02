package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
   * @param id (String)
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

  /**
   * Used to map web request to quiz entity object
   * @param request (QuizWebRequest)
   * @return Quiz object
   */
  public Quiz toQuiz(QuizWebRequest request) {
    return toValidatedQuiz(request, new Quiz());
  }

  /**
   * Private method used to validate web request and map to quiz entity object
   * @param request (QuizWebRequest)
   * @param quiz (Quiz)
   * @return Quiz object
   */
  private Quiz toValidatedQuiz(QuizWebRequest request, Quiz quiz) {
    return Optional.ofNullable(request)
            .map(validator::validate)
            .map(val -> {
              BeanUtils.copyProperties(val, quiz);
              return quiz;
            })
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }
}
