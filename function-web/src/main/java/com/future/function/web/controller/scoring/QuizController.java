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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/scoring/batches/{batchCode}/quizzes")
public class QuizController {

  private QuizService quizService;

  private QuizRequestMapper quizRequestMapper;

  @Autowired
  public QuizController(
    QuizService quizService, QuizRequestMapper quizRequestMapper
  ) {

    this.quizService = quizService;
    this.quizRequestMapper = quizRequestMapper;
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuizWebResponse> getAllQuiz(
    @PathVariable
      String batchCode,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "10")
      int size,
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
      Session session
  ) {

    return QuizResponseMapper.toQuizWebPagingResponse(
      quizService.findAllByBatchCodeAndPageable(batchCode,
                                                PageHelper.toPageable(page,
                                                                      size
                                                )
      ));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> getQuizById(
    @PathVariable
      String id,
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR })
      Session session
  ) {

    return QuizResponseMapper.toQuizWebDataResponse(quizService.findById(id));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/copy",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> copyQuiz(
    @RequestBody
      CopyQuizWebRequest request,
    @WithAnyRole(roles = Role.ADMIN)
      Session session
  ) {

    request = quizRequestMapper.validateCopyQuizWebRequest(request);
    return QuizResponseMapper.toQuizWebDataResponse(HttpStatus.CREATED,
                                                    quizService.copyQuizWithTargetBatchCode(
                                                      request.getBatchCode(),
                                                      quizService.findById(
                                                        request.getQuizId())
                                                    )
    );
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> createQuiz(
    @PathVariable("batchCode")
      String batchCode,
    @RequestBody
      QuizWebRequest quizWebRequest,
    @WithAnyRole(roles = Role.ADMIN)
      Session session
  ) {

    return QuizResponseMapper.toQuizWebDataResponse(
      HttpStatus.CREATED, quizService.createQuiz(
        quizRequestMapper.toQuiz(quizWebRequest, batchCode)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @PutMapping(path = "/{id}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuizWebResponse> updateQuiz(
    @PathVariable("batchCode")
      String batchCode,
    @PathVariable("id")
      String id,
    @RequestBody
      QuizWebRequest request,
    @WithAnyRole(roles = Role.ADMIN)
      Session session
  ) {

    return QuizResponseMapper.toQuizWebDataResponse(
      quizService.updateQuiz(quizRequestMapper.toQuiz(id, request, batchCode)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteQuizById(
    @PathVariable
      String id,
    @WithAnyRole(roles = Role.ADMIN)
      Session session
  ) {

    quizService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
