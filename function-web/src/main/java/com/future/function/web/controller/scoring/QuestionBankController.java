package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuestionBankRequestMapper;
import com.future.function.web.mapper.response.scoring.QuestionBankResponseMapper;
import com.future.function.web.model.request.scoring.QuestionBankWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuestionBankWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/scoring/question-banks")
public class QuestionBankController {

  private QuestionBankService questionBankService;

  private QuestionBankRequestMapper requestMapper;

  @Autowired
  public QuestionBankController(QuestionBankService questionBankService, QuestionBankRequestMapper requestMapper) {
    this.questionBankService = questionBankService;
    this.requestMapper = requestMapper;
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionBankWebResponse> findAllQuestionBank(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
      @WithAnyRole(roles = Role.ADMIN) Session session
  ) {
    return QuestionBankResponseMapper
        .toPagingQuestionBankWebResponse(questionBankService.findAllByPageable(PageHelper.toPageable(page, size)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionBankWebResponse> findQuestionBankById(@PathVariable String id,
      @WithAnyRole(roles = Role.ADMIN) Session session) {
    return QuestionBankResponseMapper
        .toQuestionBankWebResponse(questionBankService.findById(id));
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionBankWebResponse> createQuestionBank(@RequestBody QuestionBankWebRequest request,
      @WithAnyRole(roles = Role.ADMIN) Session session) {
    return QuestionBankResponseMapper
        .toQuestionBankWebResponse(
            HttpStatus.CREATED, questionBankService.createQuestionBank(requestMapper.toQuestionBank(request)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<QuestionBankWebResponse> updateQuestionBank(@PathVariable String id,
      @RequestBody QuestionBankWebRequest request, @WithAnyRole(roles = Role.ADMIN) Session session) {
    return QuestionBankResponseMapper
        .toQuestionBankWebResponse(questionBankService.updateQuestionBank(requestMapper.toQuestionBank(id, request)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteQuestionBankById(@PathVariable String id, @WithAnyRole(roles = Role.ADMIN) Session session) {
    questionBankService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
