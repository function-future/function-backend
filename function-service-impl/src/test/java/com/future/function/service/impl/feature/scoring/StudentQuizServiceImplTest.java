package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentQuizServiceImplTest {

    private static final String USER_ID = "student-id";
    private static final String USER_NAME = "student";

    private static final String QUIZ_ID = "quiz-id";
    private static final Integer QUIZ_TRIALS = 3;

    private static final String STUDENT_QUIZ_ID = "student-quiz-id";

    private static final String BATCH_CODE = "batch-code";
    private static final String TARGET_BATCH = "target-batch";

    private Batch batch;
    private User student;
    private Quiz quiz;
    private StudentQuiz studentQuiz;
    private Pageable pageable;
    private Page<StudentQuiz> studentQuizPage;

    @InjectMocks
    private StudentQuizServiceImpl studentQuizService;

    @Mock
    private StudentQuizRepository studentQuizRepository;

    @Mock
    private UserService userService;

    @Mock
    private BatchService batchService;

    @Mock
    private StudentQuizDetailService studentQuizDetailService;


    @Before
    public void setUp() throws Exception {

        student = User
                .builder()
                .id(USER_ID)
                .name(USER_NAME)
                .build();

        batch = Batch
                .builder()
                .code(BATCH_CODE)
                .build();

        quiz = Quiz
                .builder()
                .id(QUIZ_ID)
                .trials(QUIZ_TRIALS)
                .batch(batch)
                .build();

        studentQuiz = StudentQuiz
                .builder()
                .id(STUDENT_QUIZ_ID)
                .quiz(quiz)
                .student(student)
                .trials(QUIZ_TRIALS)
                .build();

        pageable = new PageRequest(0, 10);

        studentQuizPage = new PageImpl<>(Collections.singletonList(studentQuiz), pageable, 1);

        when(studentQuizRepository.findAllByStudentId(USER_ID, pageable))
                .thenReturn(studentQuizPage);
        when(studentQuizRepository.findByStudentIdAndQuizId(USER_ID, QUIZ_ID))
                .thenReturn(Optional.of(studentQuiz));
        when(studentQuizRepository.findByIdAndDeletedFalse(STUDENT_QUIZ_ID))
                .thenReturn(Optional.of(studentQuiz));
        when(studentQuizRepository.save(studentQuiz))
                .thenReturn(studentQuiz);
        when(studentQuizRepository.save(any(StudentQuiz.class)))
                .thenReturn(studentQuiz);
        when(userService.getStudentsByBatchCode(BATCH_CODE))
                .thenReturn(Collections.singletonList(student));
        when(userService.getStudentsByBatchCode(TARGET_BATCH))
                .thenReturn(Collections.singletonList(student));
        when(userService.getUser(USER_ID)).thenReturn(student);
        when(batchService.getBatchByCode(BATCH_CODE))
                .thenReturn(batch);
        when(batchService.getBatchByCode(TARGET_BATCH))
                .thenReturn(batch);
        when(studentQuizDetailService.createStudentQuizDetail(studentQuiz, null))
                .thenReturn(new StudentQuizDetail());

    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(studentQuizRepository, batchService, studentQuizDetailService,
                userService);
    }

    @Test
    public void findAllByStudentId() {
        Page<StudentQuiz> actual = studentQuizService.findAllByStudentId(USER_ID, pageable);
        assertThat(actual.getTotalElements()).isEqualTo(1);
        assertThat(actual.getContent().get(0).getId()).isEqualTo(STUDENT_QUIZ_ID);
        assertThat(actual.getContent().get(0).getTrials()).isEqualTo(QUIZ_TRIALS);
        verify(studentQuizRepository).findAllByStudentId(USER_ID, pageable);
    }

    @Test
    public void findById() {
        StudentQuiz studentQuiz = studentQuizService.findById(STUDENT_QUIZ_ID);
        assertThat(studentQuiz.getId()).isEqualTo(STUDENT_QUIZ_ID);
        assertThat(studentQuiz.getTrials()).isEqualTo(QUIZ_TRIALS);
        verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
    }

    @Test
    public void createStudentQuizAndSave() {
        StudentQuiz actual = studentQuizService.createStudentQuizAndSave(USER_ID, quiz);
        assertThat(actual.getQuiz().getId()).isEqualTo(QUIZ_ID);
        assertThat(actual.getQuiz().getTrials()).isEqualTo(QUIZ_TRIALS);
        assertThat(actual.getStudent().getId()).isEqualTo(USER_ID);
        assertThat(actual.getStudent().getName()).isEqualTo(USER_NAME);
        verify(userService).getUser(USER_ID);
        verify(studentQuizRepository).save(any(StudentQuiz.class));
    }

    @Test
    public void createStudentQuizByBatchCode() {
        Quiz actual = studentQuizService.createStudentQuizByBatchCode(BATCH_CODE, quiz);
        assertThat(actual.getBatch().getCode()).isEqualTo(BATCH_CODE);
        verify(userService).getStudentsByBatchCode(BATCH_CODE);
        verify(userService).getUser(USER_ID);
        verify(studentQuizDetailService).createStudentQuizDetail(studentQuiz, null);
        verify(studentQuizRepository).save(any(StudentQuiz.class));
    }

    @Test
    public void createStudentQuizByBatchCodeStudentQuizFailToSave() {
        when(userService.getStudentsByBatchCode(BATCH_CODE)).thenReturn(Collections.emptyList());
        catchException(() -> studentQuizService.createStudentQuizByBatchCode(BATCH_CODE, quiz));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
        verify(userService).getStudentsByBatchCode(BATCH_CODE);
    }

    @Test
    public void copyQuizFromBatch() {
        Batch targetBatch = Batch.builder().code(TARGET_BATCH).build();
        Quiz actual = studentQuizService.copyQuizWithTargetBatch(targetBatch, quiz);
        assertThat(actual.getTrials()).isEqualTo(QUIZ_TRIALS);
        assertThat(actual.getId()).isNotEqualTo(QUIZ_ID);
        verify(studentQuizRepository).save(any(StudentQuiz.class));
        verify(userService).getStudentsByBatchCode(TARGET_BATCH);
        verify(userService).getUser(USER_ID);
        verify(studentQuizDetailService).createStudentQuizDetail(studentQuiz, null);
    }

    @Test
    public void deleteById() {
        studentQuizService.deleteById(STUDENT_QUIZ_ID);
        studentQuiz.setDeleted(true);
        verify(studentQuizRepository).findByIdAndDeletedFalse(STUDENT_QUIZ_ID);
        verify(studentQuizRepository).save(studentQuiz);
    }

    @Test
    public void deleteByBatchCodeAndQuiz() {
        studentQuiz.setDeleted(true);
        studentQuizService.deleteByBatchCodeAndQuiz(BATCH_CODE, QUIZ_ID);
        verify(userService).getStudentsByBatchCode(BATCH_CODE);
        verify(studentQuizDetailService).deleteByStudentQuiz(studentQuiz);
        verify(studentQuizRepository).findByStudentIdAndQuizId(USER_ID, QUIZ_ID);
        verify(studentQuizRepository).save(studentQuiz);
    }
}