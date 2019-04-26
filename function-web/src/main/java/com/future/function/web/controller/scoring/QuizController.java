package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuizRequestMapper;
import com.future.function.web.mapper.response.scoring.QuizResponseMapper;
import com.future.function.web.model.request.scoring.QuizWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/scoring/quiz")
public class QuizController {

  private QuizService quizService;

  private QuizRequestMapper quizRequestMapper;

  @Autowired
  public QuizController(QuizService quizService, QuizRequestMapper quizRequestMapper) {
    this.quizService = quizService;
    this.quizRequestMapper = quizRequestMapper;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuizWebResponse> getAllQuiz(
          @RequestParam(required = false) int page,
          @RequestParam(required = false) int size,
          @RequestParam(required = false) String filter,
          @RequestParam(required = false) String search
  ) {
    return QuizResponseMapper
            .toQuizWebPagingResponse(
                    quizService
                      .findAllByPageableAndFilterAndSearch(
                              PageHelper.toPage(page, size),
                              filter,
                              search
                      )
            );
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> getQuizById(@PathVariable String id) {
    return QuizResponseMapper
            .toQuizWebDataResponse(
                    quizService
                      .findById(id)
            );
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> createQuiz(@RequestBody QuizWebRequest quizWebRequest) {
    return QuizResponseMapper
            .toQuizWebDataResponse(
                    HttpStatus.CREATED,
                    quizService
                      .createQuiz(
                              quizRequestMapper
                                .toQuiz(quizWebRequest)
                      )
            );
  }

  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> updateQuiz(
          @PathVariable String id,
          @RequestBody QuizWebRequest request
  ) {
    return QuizResponseMapper
            .toQuizWebDataResponse(
                    quizService
                      .updateQuiz(
                              quizRequestMapper
                                .toQuiz(id, request)
                      )
            );
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteQuizById(@PathVariable String id) {
    quizService.deleteById(id);
    return ResponseHelper
            .toBaseResponse(HttpStatus.OK);
  }

}
