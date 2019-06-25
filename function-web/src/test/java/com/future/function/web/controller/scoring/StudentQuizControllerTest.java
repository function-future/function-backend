package com.future.function.web.controller.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.response.scoring.StudentQuizResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuizWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(StudentQuizController.class)
public class StudentQuizControllerTest {

    private static final String QUIZ_ID = "quiz-id";
    private static final String QUIZ_TITLE = "quiz-title";
    private static final int QUIZ_TRIALS = 3;
    private static final String BATCH_CODE = "1";
    private static final String STUDENT_ID = "student-id";
    private static final String STUDENT_NAME = "student-name";
    private static final String STUDENT_QUIZ_ID = "student-quiz-id";

    private StudentQuiz studentQuiz;
    private Quiz quiz;
    private Batch batch;
    private User user;
    private StudentQuizWebResponse webResponse;
    private QuizWebResponse quizWebResponse;
    private Pageable pageable;
    private Page<StudentQuiz> studentQuizPage;

    private DataResponse<StudentQuizWebResponse> dataResponse;

    private PagingResponse<StudentQuizWebResponse> pagingResponse;

    private JacksonTester<DataResponse<StudentQuizWebResponse>> dataResponseJacksonTester;

    private JacksonTester<PagingResponse<StudentQuizWebResponse>> pagingResponseJacksonTester;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentQuizService studentQuizService;

    @Before
    public void setUp() throws Exception {

        JacksonTester.initFields(this, ObjectMapper::new);

        batch = Batch
                .builder()
                .code(BATCH_CODE)
                .build();

        user = User.builder()
                .id(STUDENT_ID)
                .name(STUDENT_NAME)
                .batch(batch)
                .build();

        quiz = Quiz.builder()
                .id(QUIZ_ID)
                .trials(QUIZ_TRIALS)
                .batch(batch)
                .build();

        studentQuiz = StudentQuiz
                .builder()
                .student(user)
                .quiz(quiz)
                .id(STUDENT_QUIZ_ID)
                .trials(QUIZ_TRIALS)
                .done(false)
                .build();

        quizWebResponse = QuizWebResponse
                .builder()
                .id(QUIZ_ID)
                .title(QUIZ_TITLE)
                .trials(QUIZ_TRIALS)
                .batchCode(BATCH_CODE)
                .build();

        webResponse = StudentQuizWebResponse
                .builder()
                .id(STUDENT_QUIZ_ID)
                .quiz(quizWebResponse)
                .build();

        pageable = new PageRequest(0, 10);

        studentQuizPage = new PageImpl<>(Collections.singletonList(studentQuiz), pageable, 1);

        dataResponse = StudentQuizResponseMapper
                .toStudentQuizWebResponse(studentQuiz);

        pagingResponse = StudentQuizResponseMapper
                .toPagingStudentQuizWebResponse(studentQuizPage);

        when(studentQuizService.findAllByStudentId(STUDENT_ID, pageable))
                .thenReturn(studentQuizPage);
        when(studentQuizService.findById(STUDENT_QUIZ_ID))
                .thenReturn(studentQuiz);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(studentQuizService);
    }

    @Test
    public void getAllStudentQuiz() throws Exception {
        mockMvc.perform(
                get("/api/scoring/students/" + STUDENT_ID + "/quizzes")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        pagingResponseJacksonTester.write(
                                pagingResponse).getJson()
                ));
        verify(studentQuizService).findAllByStudentId(STUDENT_ID, pageable);
    }

    @Test
    public void getStudentQuizById() throws Exception {
        mockMvc.perform(
                get("/api/scoring/students/" + STUDENT_ID + "/quizzes/" + STUDENT_QUIZ_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(
                                dataResponse).getJson()
                ));
        verify(studentQuizService).findById(STUDENT_QUIZ_ID);
    }
}