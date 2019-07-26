package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.request.scoring.StudentQuestionRequestMapper;
import com.future.function.web.mapper.response.scoring.StudentQuizDetailResponseMapper;
import com.future.function.web.model.request.scoring.StudentQuestionWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuestionWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizDetailWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;


@RestController
@RequestMapping(path = "/api/scoring/students/{studentId}/quizzes/{studentQuizId}/questions")
public class StudentQuestionController {

  private StudentQuizService studentQuizService;

  private StudentQuestionRequestMapper studentQuestionRequestMapper;

  @Autowired
  public StudentQuestionController(StudentQuizService studentQuizService,
                                   StudentQuestionRequestMapper studentQuestionRequestMapper) {
    this.studentQuizService = studentQuizService;
    this.studentQuestionRequestMapper = studentQuestionRequestMapper;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<StudentQuestionWebResponse> findUnansweredStudentQuestionsByStudentQuizId(
          @PathVariable String studentQuizId, @WithAnyRole(roles = Role.STUDENT) Session session) {
    return StudentQuizDetailResponseMapper
        .toStudentQuestionWebResponses(
            studentQuizService
                .findAllUnansweredQuestionByStudentQuizId(studentQuizId, session.getUserId()));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<StudentQuizDetailWebResponse> postAnswersForQuestions(@PathVariable String studentQuizId,
                                                                            @RequestBody List<StudentQuestionWebRequest> answerRequests,
                                                                            @WithAnyRole(roles = Role.STUDENT) Session session) {
    return StudentQuizDetailResponseMapper
        .toStudentQuizDetailWebResponse(
            studentQuizService
                .answerQuestionsByStudentQuizId(
                    studentQuizId,
                    session.getUserId(),
                    studentQuestionRequestMapper
                        .toStudentQuestionList(answerRequests)));
  }
}
