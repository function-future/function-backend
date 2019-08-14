package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.CopyQuizWebRequest;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class QuizRequestMapper {

  private RequestValidator validator;

  @Autowired
  public QuizRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public Quiz toQuiz(String id, QuizWebRequest request, String batchCode) {
      Quiz quiz = toValidatedQuiz(request, batchCode);
      quiz.setId(id);
      return quiz;
  }

  public CopyQuizWebRequest validateCopyQuizWebRequest(CopyQuizWebRequest request) {
    return validator.validate(request);
  }

  public Quiz toQuiz(QuizWebRequest request, String batchCode) {
      return toValidatedQuiz(request, batchCode);
  }

  private Quiz toValidatedQuiz(QuizWebRequest request, String batchCode) {
      request = validator.validate(request);
      Quiz quiz = new Quiz();
      BeanUtils.copyProperties(request, quiz);
      Batch batch = Batch.builder().code(batchCode).build();
      quiz.setBatch(batch);
      quiz.setQuestionBanks(buildQuestionBanks(request.getQuestionBanks()));
      return quiz;
  }

  private List<QuestionBank> buildQuestionBanks(List<String> questionBankIds) {
    return questionBankIds
        .stream()
        .map(id -> QuestionBank.builder().id(id).build())
        .collect(Collectors.toList());
  }
}
