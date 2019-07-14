package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuestionRequestMapper;
import com.future.function.web.mapper.response.scoring.QuestionResponseMapper;
import com.future.function.web.model.request.scoring.QuestionWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuestionWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/scoring/question-banks/{questionBankId}/questions")
public class QuestionController {

  private QuestionService questionService;

  private QuestionRequestMapper questionRequestMapper;

  @Autowired
  public QuestionController(QuestionService questionService, QuestionRequestMapper questionRequestMapper) {
    this.questionService = questionService;
    this.questionRequestMapper = questionRequestMapper;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public PagingResponse<QuestionWebResponse> getAllQuestionForQuestionBank(@PathVariable String questionBankId,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           Session session) {
    return QuestionResponseMapper
        .toQuestionPagingResponse(
            questionService.findAllByQuestionBankId(questionBankId, PageHelper.toPageable(page, size)));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuestionWebResponse> getQuestionDetailById(@PathVariable String questionId, Session session) {
    return QuestionResponseMapper.toQuestionWebResponse(questionService.findById(questionId));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuestionWebResponse> createQuestionForQuestionBank(@PathVariable String questionBankId,
      @RequestBody QuestionWebRequest request, Session session) {
    return QuestionResponseMapper
        .toQuestionWebResponse(
            HttpStatus.CREATED,
            questionService.createQuestion(questionRequestMapper.toQuestion(request), questionBankId));
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(path = "/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuestionWebResponse> updateQuestionForQuestionBank(@PathVariable String questionBankId,
      @PathVariable String questionId,
      @RequestBody QuestionWebRequest request, Session session) {
    return QuestionResponseMapper
        .toQuestionWebResponse(
            HttpStatus.OK,
            questionService.updateQuestion(questionRequestMapper.toQuestion(request, questionId), questionBankId));
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(path = "/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public BaseResponse deleteQuestionFromQuestionBank(@PathVariable String questionId, Session session) {
    questionService.deleteById(questionId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
