package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.session.annotation.WithAnyRole;
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
@RequestMapping(value = "/api/question-banks")
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
  @WithAnyRole(roles = Role.ADMIN)
  public PagingResponse<QuestionBankWebResponse> findAllQuestionBank(
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size
  ) {
    return QuestionBankResponseMapper
        .toPagingQuestionBankWebResponse(questionBankService.findAllByPageable(PageHelper.toPageable(page, size)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuestionBankWebResponse> findQuestionBankById(@PathVariable String id) {
    return QuestionBankResponseMapper
        .toQuestionBankWebResponse(questionBankService.findById(id));
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuestionBankWebResponse> createQuestionBank(@RequestBody QuestionBankWebRequest request) {
    return QuestionBankResponseMapper
        .toQuestionBankWebResponse(
            HttpStatus.CREATED, questionBankService.createQuestionBank(requestMapper.toQuestionBank(request)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public DataResponse<QuestionBankWebResponse> updateQuestionBank(@PathVariable String id,
      @RequestBody QuestionBankWebRequest request) {
    return QuestionBankResponseMapper
        .toQuestionBankWebResponse(questionBankService.updateQuestionBank(requestMapper.toQuestionBank(id, request)));
  }

  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.ADMIN)
  public BaseResponse deleteQuestionBankById(@PathVariable String id) {
    questionBankService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
