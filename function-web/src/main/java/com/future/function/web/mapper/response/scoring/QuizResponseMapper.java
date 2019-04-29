package com.future.function.web.mapper.response.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

/**
 * Static class used to map quiz entity object to web response object
 */
public class QuizResponseMapper {

  /**
   * Used to map quiz entity object into DataResponse of QuizWebResponse
   * @param quiz (Quiz)
   * @return DataResponse<QuizWebResponse>
   */
  public static DataResponse<QuizWebResponse> toQuizWebDataResponse(Quiz quiz) {
    return toQuizWebDataResponse(HttpStatus.OK, quiz);
  }

  /**
   * Used to map quiz entity object with http status into DataResponse of QuizWebResponse
   * @param httpStatus (HttpStatus)
   * @param quiz (Quiz)
   * @return DataResponse<QuizWebResponse>
   */
  public static DataResponse<QuizWebResponse> toQuizWebDataResponse(HttpStatus httpStatus, Quiz quiz) {
    return ResponseHelper.toDataResponse(httpStatus, buildQuizWebResponse(quiz));
  }

  /**
   * Used to create new QuizWebResponse and map its attribute from quiz parameter
   * @param quiz (Quiz)
   * @return QuizWebResponse object
   */
  private static QuizWebResponse buildQuizWebResponse(Quiz quiz) {
    return Optional.ofNullable(quiz)
            .map(val -> {
              QuizWebResponse response = new QuizWebResponse();
              BeanUtils.copyProperties(val, response);
              return response;
            })
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  /**
   * Used to map Page of Quiz entity object into PagingResponse of QuizWebResponse
   * @param quizPage (Page<Quiz>)
   * @return PagingResponse<QuizWebResponse>
   */
  public static PagingResponse<QuizWebResponse> toQuizWebPagingResponse(Page<Quiz> quizPage) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toQuizWebResponseList(quizPage), PageHelper.toPaging(quizPage));
  }

  /**
   * Used to map Page of Quiz entity object into List of QuizWebResponse
   * @param quizPage (Page<Quiz>)
   * @return List<QuizWebResponse>
   */
  private static List<QuizWebResponse> toQuizWebResponseList(Page<Quiz> quizPage) {
    return quizPage
            .getContent()
            .stream()
            .map(QuizResponseMapper::buildQuizWebResponse)
            .collect(Collectors.toList());
  }

}
