package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.scoring.StudentQuizResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/students/{studentId}/quizzes")
public class StudentQuizController {

  private StudentQuizService studentQuizService;

  @Autowired
  public StudentQuizController(StudentQuizService studentQuizService) {
    this.studentQuizService = studentQuizService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
  public PagingResponse<StudentQuizWebResponse> getAllStudentQuiz(@PathVariable String studentId,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      Session session) {
    return StudentQuizResponseMapper
        .toPagingStudentQuizWebResponse(
            studentQuizService
                .findAllByStudentId(studentId, PageHelper.toPageable(page, size), session.getUserId())
        );

  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/{studentQuizId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
  public DataResponse<StudentQuizWebResponse> getStudentQuizById(@PathVariable(value = "studentQuizId") String studentQuizId,
      Session session) {
    return StudentQuizResponseMapper
        .toStudentQuizWebResponse(
            studentQuizService
                .findById(studentQuizId, session.getUserId())
        );
  }

}
