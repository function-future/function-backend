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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/scoring/students/{studentId}/quizzes/{studentQuizId}/questions")
public class StudentQuestionController {

  @Autowired
  private StudentQuizService studentQuizService;

  @Autowired
  private StudentQuestionRequestMapper studentQuestionRequestMapper;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.STUDENT)
  public PagingResponse<StudentQuestionWebResponse> findStudentQuestionsByStudentQuizId(@PathVariable(value = "studentQuizId")
      String studentQuizId, Session session) {
    return StudentQuizDetailResponseMapper
        .toStudentQuestionWebResponses(
            studentQuizService
                .findAllQuestionsByStudentQuizId(studentQuizId, session.getUserId()));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.STUDENT)
  public PagingResponse<StudentQuestionWebResponse> findUnansweredStudentQuestionsByStudentQuizId(
      @PathVariable(value = "studentQuizId") String studentQuizId, Session session) {
    return StudentQuizDetailResponseMapper
        .toStudentQuestionWebResponses(
            studentQuizService
                .findAllUnansweredQuestionByStudentQuizId(studentQuizId, session.getUserId()));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = Role.STUDENT)
  public DataResponse<StudentQuizDetailWebResponse> postAnswersForQuestions(@PathVariable(value = "studentQuizId") String studentQuizId,
      @RequestBody List<StudentQuestionWebRequest> answerRequests, Session session) {
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
