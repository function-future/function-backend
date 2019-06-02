package com.future.function.web.controller.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.scoring.*;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.web.mapper.request.scoring.StudentQuestionRequestMapper;
import com.future.function.web.mapper.response.scoring.StudentQuizDetailResponseMapper;
import com.future.function.web.model.request.scoring.StudentQuestionWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuestionWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizDetailWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentQuestionController.class)
public class StudentQuestionControllerTest {

    private static final String STUDENT_QUIZ_ID = "student-quiz-id";
    private static final String STUDENT_QUIZ_DETAIL_ID = "student-quiz-detail-id";
    private static final String STUDENT_QUESTION_ID = "student-question-id";
    private static final String QUESTION_ID = "question-id";
    private static final String OPTION_ID = "option-id";

    private StudentQuiz studentQuiz;
    private StudentQuizDetail studentQuizDetail;
    private StudentQuestion studentQuestion;
    private StudentQuestionWebRequest studentQuestionWebRequest;
    private Question question;
    private Option option;

    private DataResponse<StudentQuizDetailWebResponse> studentQuizDetailWebResponseDataResponse;
    private PagingResponse<StudentQuestionWebResponse> pagingResponse;

    private JacksonTester<DataResponse<StudentQuizDetailWebResponse>> dataResponseJacksonTester;
    private JacksonTester<PagingResponse<StudentQuestionWebResponse>> pagingResponseJacksonTester;
    private JacksonTester<List<StudentQuestionWebRequest>> webRequestJacksonTester;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentQuizDetailService studentQuizDetailService;

    @MockBean
    private StudentQuestionRequestMapper requestMapper;

    @Before
    public void setUp() throws Exception {

        JacksonTester.initFields(this, ObjectMapper::new);

        studentQuiz = StudentQuiz
                .builder()
                .id(STUDENT_QUIZ_ID)
                .build();

        studentQuizDetail = StudentQuizDetail
                .builder()
                .id(STUDENT_QUIZ_DETAIL_ID)
                .studentQuiz(studentQuiz)
                .build();

        question = Question
                .builder()
                .id(QUESTION_ID)
                .build();

        option = Option
                .builder()
                .id(OPTION_ID)
                .question(question)
                .build();

        question.setOptions(Collections.singletonList(option));

        studentQuestion = StudentQuestion
                .builder()
                .id(STUDENT_QUESTION_ID)
                .number(1)
                .studentQuizDetail(studentQuizDetail)
                .correct(true)
                .question(question)
                .option(option)
                .build();

        studentQuestionWebRequest = StudentQuestionWebRequest
                .builder()
                .number(1)
                .optionId(OPTION_ID)
                .build();

        studentQuizDetailWebResponseDataResponse = StudentQuizDetailResponseMapper
                .toStudentQuizDetailWebResponse(studentQuizDetail);

        pagingResponse = StudentQuizDetailResponseMapper
                .toStudentQuestionWebResponses(Collections.singletonList(studentQuestion));

        when(studentQuizDetailService.answerStudentQuiz(STUDENT_QUIZ_ID, Collections.singletonList(studentQuestion)))
                .thenReturn(studentQuizDetail);
        when(studentQuizDetailService.findAllQuestionsByStudentQuizId(STUDENT_QUIZ_ID))
                .thenReturn(Collections.singletonList(studentQuestion));
        when(requestMapper.toStudentQuestionList(Collections.singletonList(studentQuestionWebRequest)))
                .thenReturn(Collections.singletonList(studentQuestion));


    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(studentQuizDetailService, requestMapper);
    }

    @Test
    public void findStudentQuestionsByStudentQuizIdTest() throws Exception {
        mockMvc.perform(
                get("/api/scoring/students/studentId/quizzes/" + STUDENT_QUIZ_ID + "/questions"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        pagingResponseJacksonTester.write(
                                pagingResponse).getJson()
                ));
        verify(studentQuizDetailService).findAllQuestionsByStudentQuizId(STUDENT_QUIZ_ID);
    }

    @Test
    public void postAnswersForQuestionsTest() throws Exception {
        mockMvc.perform(
                post("/api/scoring/students/studentId/quizzes/" + STUDENT_QUIZ_ID + "/questions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(webRequestJacksonTester
                                .write(Collections.singletonList(studentQuestionWebRequest))
                                .getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(
                                studentQuizDetailWebResponseDataResponse).getJson()
                ));
        verify(studentQuizDetailService).answerStudentQuiz(STUDENT_QUIZ_ID, Collections.singletonList(studentQuestion));
        verify(requestMapper).toStudentQuestionList(Collections.singletonList(studentQuestionWebRequest));
    }
}