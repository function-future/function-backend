package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.StudentQuizRequestMapper;
import com.future.function.web.mapper.response.scoring.StudentQuizResponseMapper;
import com.future.function.web.model.request.scoring.StudentQuizWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizWebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scoring/students/{studentId}/quizzes")
public class StudentQuizController {

    private StudentQuizService studentQuizService;

    private StudentQuizRequestMapper requestMapper;

    private UserService userService;

    private QuizService quizService;

    public StudentQuizController(StudentQuizService studentQuizService,
                                 StudentQuizRequestMapper requestMapper,
                                 UserService userService,
                                 QuizService quizService) {
        this.studentQuizService = studentQuizService;
        this.requestMapper = requestMapper;
        this.userService = userService;
        this.quizService = quizService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<StudentQuizWebResponse> getAllStudentQuiz(@PathVariable String studentId,
                                                                    @RequestParam(required = false,
                                                                            defaultValue = "1") int page,
                                                                    @RequestParam(required = false,
                                                                            defaultValue = "10") int size) {
        return StudentQuizResponseMapper
                .toPagingStudentQuizWebResponse(
                        studentQuizService
                                .findAllByStudentId(studentId, PageHelper.toPage(page, size))
                );

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{studentQuizId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<StudentQuizWebResponse> getStudentQuizById(@PathVariable String studentQuizId) {
        return StudentQuizResponseMapper
                .toStudentQuizWebResponse(
                        studentQuizService
                                .findById(studentQuizId)
                );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<StudentQuizWebResponse> createStudentQuiz(@RequestBody StudentQuizWebRequest request) {
        return StudentQuizResponseMapper
                .toStudentQuizWebResponse(
                        HttpStatus.CREATED,
                        userService.getUsers(Role.STUDENT, PageHelper.toPage(1, 10))
                                .getContent()
                                .stream()
                                .forEach(student -> studentQuizService
                                        .createStudentQuiz(
                                                student.getId(),
                                                quizService.findById(request.getQuizId())))
                );
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{studentQuizId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse deleteStudentQuizById(@PathVariable String studentQuizId) {
        return ResponseHelper.toBaseResponse(HttpStatus.OK);
    }

}
