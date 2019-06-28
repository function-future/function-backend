package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuestionWebResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestionResponseMapper {

  public static DataResponse<QuestionWebResponse> toQuestionWebResponse(Question question) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildQuestionWebResponse(question, question.getOptions()));
  }

  public static DataResponse<QuestionWebResponse> toQuestionWebResponse(HttpStatus httpStatus, Question question) {
    return ResponseHelper.toDataResponse(httpStatus, buildQuestionWebResponse(question, question.getOptions()));
  }

  private static QuestionWebResponse buildQuestionWebResponse(Question question, List<Option> options) {
    return QuestionWebResponse.builder()
        .id(question.getId())
        .text(question.getText())
        .options(OptionResponseMapper.toListOfOptionWebResponse(options))
        .build();
  }

  public static PagingResponse<QuestionWebResponse> toQuestionPagingResponse(Page<Question> questionPage) {
    List<QuestionWebResponse> responseList = getQuestionWebResponseList(questionPage.getContent());
    return ResponseHelper.toPagingResponse(HttpStatus.OK, responseList, PageHelper.toPaging(questionPage));
  }

  private static List<QuestionWebResponse> getQuestionWebResponseList(List<Question> questionList) {
    return questionList
        .stream()
        .map(question -> QuestionResponseMapper.buildQuestionWebResponse(question, question.getOptions()))
        .collect(Collectors.toList());
  }

}
