package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuestionWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionResponseMapper {

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
        List<QuestionWebResponse> responseList = questionPage.getContent()
                .stream()
                .map(question -> QuestionResponseMapper.buildQuestionWebResponse(question, question.getOptions()))
                .collect(Collectors.toList());
        return ResponseHelper.toPagingResponse(HttpStatus.OK, responseList, PageHelper.toPaging(questionPage));
    }

}
