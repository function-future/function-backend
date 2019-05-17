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
import org.springframework.web.bind.annotation.*;

/**
 * Controller class used to interact with http request for quiz entity
 */
@RestController
@RequestMapping(path = "/api/scoring/quizzes")
public class QuizController {

  private QuizService quizService;

  private QuizRequestMapper quizRequestMapper;

  @Autowired
  public QuizController(QuizService quizService, QuizRequestMapper quizRequestMapper) {
    this.quizService = quizService;
    this.quizRequestMapper = quizRequestMapper;
  }

  /**
   * Used to get list of quiz entity object with paging, filter, and search parameters
   * @param page (int, required false)
   * @param size (int, required false)
   * @param filter (String, required false)
   * @param search (String, required false)
   * @return PagingResponse<QuizWebResponse> with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuizWebResponse> getAllQuiz(
          @RequestParam(required = false, defaultValue = "1") int page,
          @RequestParam(required = false, defaultValue = "10") int size,
          @RequestParam(required = false, defaultValue = "") String filter,
          @RequestParam(required = false, defaultValue = "") String search
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

  /**
   * Used to get specific quiz by passing the id in the path variable
   * @param id (String)
   * @return DataResponse<QuizWebResponse> with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> getQuizById(@PathVariable String id) {
    return QuizResponseMapper
            .toQuizWebDataResponse(
                    quizService
                      .findById(id)
            );
  }

  /**
   * Used to create new quiz by passing the QuizWebRequest object as JSON
   * @param quizWebRequest (JSON)
   * @return DataResponse<QuizWebResponse> with status CREATED
   */
  @ResponseStatus(value = HttpStatus.CREATED)
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

  /**
   * Used to update existing quiz by passing the QuizWebRequest object in the body and quiz id in path variable
   * @param id (String)
   * @param request (JSON)
   * @return DataResponse<QuizWebResponse> with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
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

  /**
   * Used to delete existing quiz by passing the quiz id in path variable
   * @param id (String)
   * @return BaseResponse with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteQuizById(@PathVariable String id) {
    quizService.deleteById(id);
    return ResponseHelper
            .toBaseResponse(HttpStatus.OK);
  }

}
