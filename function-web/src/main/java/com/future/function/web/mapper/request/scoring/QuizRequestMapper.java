package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuizRequestMapper {

  private ObjectValidator validator;

  private WebRequestMapper requestMapper;

  @Autowired
  public QuizRequestMapper(ObjectValidator validator, WebRequestMapper requestMapper) {
    this.validator = validator;
    this.requestMapper = requestMapper;
  }

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

  public Quiz toQuiz(QuizWebRequest request) {
    return toValidatedQuiz(request, new Quiz());
  }

  private Quiz toValidatedQuiz(QuizWebRequest request, Quiz quiz) {
    return Optional.of(request)
            .map(validator::validate)
            .map(val -> {
              BeanUtils.copyProperties(val, quiz);
              return quiz;
            })
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  //TODO verify if quiz controller will pass string data json or not
//  private QuizWebRequest toQuizWebRequest(String data) {
//    return requestMapper.toWebRequestObject(data, QuizWebRequest.class);
//  }
}
