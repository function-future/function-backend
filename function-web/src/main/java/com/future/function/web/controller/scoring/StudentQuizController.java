package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.response.scoring.StudentQuizResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/quizzes/{quizId}/student")
public class StudentQuizController {

  private StudentQuizService studentQuizService;

  @Autowired
  public StudentQuizController(StudentQuizService studentQuizService) {

    this.studentQuizService = studentQuizService;
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<StudentQuizWebResponse> getOrCreateStudentQuizByQuizId(
    @PathVariable
      String quizId,
    @WithAnyRole(roles = Role.STUDENT)
      Session session
  ) {
    return StudentQuizResponseMapper.toStudentQuizWebResponse(
      studentQuizService.findOrCreateByStudentIdAndQuizId(session.getUserId(), quizId));
  }

}
