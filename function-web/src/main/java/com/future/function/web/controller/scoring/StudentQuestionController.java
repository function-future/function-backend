package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
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


@RestController
@RequestMapping(path = "/api/scoring/students/{studentId}/quizzes/{studentQuizId}/questions")
public class StudentQuestionController {

    @Autowired
    private StudentQuizDetailService studentQuizDetailService;

    @Autowired
    private StudentQuestionRequestMapper studentQuestionRequestMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<StudentQuestionWebResponse> findStudentQuizDetailByStudentQuizId(@PathVariable(value = "studentQuizId")
                                                                                                   String studentQuizId) {
        return StudentQuizDetailResponseMapper
                .toStudentQuestionWebResponses(
                        studentQuizDetailService
                                .findAllQuestionsByStudentQuizId(studentQuizId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<StudentQuizDetailWebResponse> postAnswersForQuestions(@PathVariable(value = "studentQuizId") String studentQuizId,
                                                                              @RequestBody List<StudentQuestionWebRequest> answerRequests) {
        return StudentQuizDetailResponseMapper
                .toStudentQuizDetailWebResponse(
                        studentQuizDetailService
                                .answerStudentQuiz(
                                        studentQuizId,
                                        studentQuestionRequestMapper
                                                .toStudentQuestionList(answerRequests)));
    }
}
