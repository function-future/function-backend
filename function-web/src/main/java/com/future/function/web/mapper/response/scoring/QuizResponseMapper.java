package com.future.function.web.mapper.response.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizResponseMapper {

  public static DataResponse<QuizWebResponse> toQuizWebDataResponse(Quiz quiz) {

    return toQuizWebDataResponse(HttpStatus.OK, quiz);
  }

  public static DataResponse<QuizWebResponse> toQuizWebDataResponse(
    HttpStatus httpStatus, Quiz quiz
  ) {

    return ResponseHelper.toDataResponse(
      httpStatus, buildQuizWebResponse(quiz));
  }

  private static QuizWebResponse buildQuizWebResponse(Quiz quiz) {

    return Optional.ofNullable(quiz)
      .map(val -> {
        QuizWebResponse response = new QuizWebResponse();
        BeanUtils.copyProperties(val, response);
        response.setQuestionBanks(getQuestionBankIds(quiz));
        response.setBatchCode(quiz.getBatch()
                                .getCode());
        return response;
      })
      .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private static List<String> getQuestionBankIds(Quiz quiz) {

    if (quiz.getQuestionBanks() != null) {
      return quiz.getQuestionBanks()
        .stream()
        .map(QuestionBank::getId)
        .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  public static PagingResponse<QuizWebResponse> toQuizWebPagingResponse(
    Page<Quiz> quizPage
  ) {

    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, toQuizWebResponseList(quizPage),
      PageHelper.toPaging(quizPage)
    );
  }

  private static List<QuizWebResponse> toQuizWebResponseList(
    Page<Quiz> quizPage
  ) {

    return quizPage.getContent()
      .stream()
      .map(QuizResponseMapper::buildQuizWebResponse)
      .collect(Collectors.toList());
  }

}
