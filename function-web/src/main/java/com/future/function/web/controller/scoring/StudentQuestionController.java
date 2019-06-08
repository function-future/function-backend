package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.StudentQuizService;
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
    private StudentQuizService studentQuizService;

    @Autowired
    private StudentQuestionRequestMapper studentQuestionRequestMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<StudentQuestionWebResponse> findStudentQuestionsByStudentQuizId(@PathVariable(value = "studentQuizId")
                                                                                                   String studentQuizId) {
        return StudentQuizDetailResponseMapper
                .toStudentQuestionWebResponses(
                        studentQuizService
                                .findAllQuestionsByStudentQuizId(studentQuizId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<StudentQuestionWebResponse> findUnansweredStudentQuestionsByStudentQuizId(
            @PathVariable(value = "studentQuizId") String studentQuizId) {
        return StudentQuizDetailResponseMapper
                .toStudentQuestionWebResponses(
                        studentQuizService
                                .findAllUnansweredQuestionByStudentQuizId(studentQuizId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<StudentQuizDetailWebResponse> postAnswersForQuestions(@PathVariable(value = "studentQuizId") String studentQuizId,
                                                                              @RequestBody List<StudentQuestionWebRequest> answerRequests) {
        return StudentQuizDetailResponseMapper
                .toStudentQuizDetailWebResponse(
                        studentQuizService
                                .answerQuestionsByStudentQuizId(
                                        studentQuizId,
                                        studentQuestionRequestMapper
                                                .toStudentQuestionList(answerRequests)));
    }
}
