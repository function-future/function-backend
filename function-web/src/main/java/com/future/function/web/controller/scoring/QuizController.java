package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuizRequestMapper;
import com.future.function.web.mapper.response.scoring.QuizResponseMapper;
import com.future.function.web.model.request.scoring.CopyQuizWebRequest;
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

/**
 * Controller class used to interact with http request for quiz entity
 */
@RestController
@RequestMapping(path = "/api/scoring/batches/{batchCode}/quizzes")
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
   *
   * @param page   (int, required false)
   * @param size   (int, required false)
   * @return PagingResponse<QuizWebResponse> with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
  public PagingResponse<QuizWebResponse> getAllQuiz(
      @PathVariable(value = "batchCode") String batchCode,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      Session session
  ) {
    return QuizResponseMapper
        .toQuizWebPagingResponse(
            quizService.findAllByBatchCodeAndPageable(batchCode, PageHelper.toPageable(page, size)));
  }

  /**
   * Used to get specific quiz by passing the id in the path variable
   *
   * @param id (String)
   * @return DataResponse<QuizWebResponse> with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
  public DataResponse<QuizWebResponse> getQuizById(@PathVariable String id, Session session) {
    return QuizResponseMapper.toQuizWebDataResponse(quizService.findById(id));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/copy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuizWebResponse> copyQuiz(@RequestBody CopyQuizWebRequest request, Session session) {
    request = quizRequestMapper.validateCopyQuizWebRequest(request);
    return QuizResponseMapper
        .toQuizWebDataResponse(
            HttpStatus.CREATED,
            quizService.copyQuizWithTargetBatch(request.getBatchCode(), quizService.findById(request.getQuizId())));
  }

  /**
   * Used to create new quiz by passing the QuizWebRequest object as JSON
   *
   * @param quizWebRequest (JSON)
   * @return DataResponse<QuizWebResponse> with status CREATED
   */
  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuizWebResponse> createQuiz(@RequestBody QuizWebRequest quizWebRequest, Session session) {
    return QuizResponseMapper
        .toQuizWebDataResponse(HttpStatus.CREATED, quizService.createQuiz(quizRequestMapper.toQuiz(quizWebRequest)));
  }

  /**
   * Used to update existing quiz by passing the QuizWebRequest object in the body and quiz id in path variable
   *
   * @param id      (String)
   * @param request (JSON)
   * @return DataResponse<QuizWebResponse> with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuizWebResponse> updateQuiz(@PathVariable String id, @RequestBody QuizWebRequest request,
      Session session) {
    return QuizResponseMapper.toQuizWebDataResponse(quizService.updateQuiz(quizRequestMapper.toQuiz(id, request)));
  }

  /**
   * Used to delete existing quiz by passing the quiz id in path variable
   *
   * @param id (String)
   * @return BaseResponse with status OK
   */
  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public BaseResponse deleteQuizById(@PathVariable String id, Session session) {
    quizService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
