package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.scoring.*;
import com.future.function.repository.feature.scoring.StudentQuizDetailRepository;
import com.future.function.service.api.feature.scoring.StudentQuestionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentQuizDetailServiceImplTest {

    private static final String STUDENT_QUIZ_ID = "student-quiz-id";
    private static final String STUDENT_QUIZ_DETAIL_ID = "student-detail-quiz-id";
    private static final String STUDENT_QUESTION_ID = "student-question-id";

    private static final String QUESTION_ID = "question-id";
    private static final String OPTION_ID = "option-id";
    private static final String OPTION_LABEL = "option-label";
    private static final String QUESTION_TEXT = "question-text";

    private static final String QUIZ_ID = "quiz-id";
    private static final String QUESTION_BANK_ID = "question-bank-id";
    private static final int QUESTION_COUNT = 1;

    private Quiz quiz;
    private QuestionBank questionBank;
    private StudentQuiz studentQuiz;
    private StudentQuizDetail studentQuizDetail;
    private StudentQuestion studentQuestion;
    private Option option;
    private Question question;

    @InjectMocks
    private StudentQuizDetailServiceImpl studentQuizDetailService;

    @Mock
    private StudentQuizDetailRepository studentQuizDetailRepository;

    @Mock
    private StudentQuestionService studentQuestionService;

    @Before
    public void setUp() throws Exception {

        questionBank = QuestionBank
                .builder()
                .id(QUESTION_BANK_ID)
                .build();

        question = Question
                .builder()
                .id(QUESTION_ID)
                .text(QUESTION_TEXT)
                .questionBank(questionBank)
                .build();

        option = Option
                .builder()
                .id(OPTION_ID)
                .label(OPTION_LABEL)
                .correct(true)
                .question(question)
                .build();

        quiz = Quiz
                .builder()
                .id(QUIZ_ID)
                .questionCount(QUESTION_COUNT)
                .questionBanks(Collections.singletonList(questionBank))
                .build();

        studentQuiz = StudentQuiz
                .builder()
                .id(STUDENT_QUIZ_ID)
                .quiz(quiz)
                .build();

        studentQuizDetail = StudentQuizDetail
                .builder()
                .id(STUDENT_QUIZ_DETAIL_ID)
                .studentQuiz(studentQuiz)
                .build();

        studentQuestion = StudentQuestion
                .builder()
                .id(STUDENT_QUESTION_ID)
                .number(1)
                .studentQuizDetail(studentQuizDetail)
                .question(question)
                .option(option)
                .build();

        when(studentQuizDetailRepository.findFirstByStudentQuizId(STUDENT_QUIZ_ID))
                .thenReturn(Optional.of(studentQuizDetail));
        when(studentQuizDetailRepository.findByIdAndDeletedFalse(STUDENT_QUIZ_DETAIL_ID))
                .thenReturn(Optional.of(studentQuizDetail));
        when(studentQuizDetailRepository.save(studentQuizDetail)).thenReturn(studentQuizDetail);
        when(studentQuizDetailRepository.save(any(StudentQuizDetail.class))).thenReturn(studentQuizDetail);
        when(studentQuestionService.createStudentQuestionsByStudentQuizDetail(studentQuizDetail,
                Collections.singletonList(studentQuestion))).thenReturn(Collections.singletonList(studentQuestion));
        when(studentQuestionService
                .findAllQuestionsFromMultipleQuestionBank(true, Collections.singletonList(questionBank), QUESTION_COUNT))
                .thenReturn(Collections.singletonList(question));
        when(studentQuestionService.createStudentQuestionsFromQuestionList(Collections.singletonList(question), studentQuizDetail))
                .thenReturn(Collections.singletonList(studentQuestion));
        when(studentQuestionService.postAnswerForAllStudentQuestion(Collections.singletonList(studentQuestion)))
                .thenReturn(100);
        when(studentQuestionService.findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID))
                .thenReturn(Collections.singletonList(studentQuestion));
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(studentQuizDetailRepository, studentQuestionService);
    }

    @Test
    public void findLatestByStudentQuizId() {
        StudentQuizDetail actual = studentQuizDetailService.findLatestByStudentQuizId(STUDENT_QUIZ_ID);
        assertThat(actual.getId()).isEqualTo(STUDENT_QUIZ_DETAIL_ID);
        assertThat(actual.getStudentQuiz().getId()).isEqualTo(STUDENT_QUIZ_ID);
        verify(studentQuizDetailRepository).findFirstByStudentQuizId(STUDENT_QUIZ_ID);
    }

    @Test
    public void findAllQuestionsByStudentQuizId() {
        List<StudentQuestion> actual = studentQuizDetailService.findAllQuestionsByStudentQuizId(STUDENT_QUIZ_ID);
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getId()).isEqualTo(STUDENT_QUESTION_ID);
        verify(studentQuizDetailRepository).findFirstByStudentQuizId(STUDENT_QUIZ_ID);
        verify(studentQuestionService).findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID);
    }

    @Test
    public void findAllUnansweredQuestionsByStudentQuizId() {
        List<StudentQuestion> actual = studentQuizDetailService.findAllUnansweredQuestionsByStudentQuizId(STUDENT_QUIZ_ID);
        assertThat(actual.get(0).getNumber()).isEqualTo(1);
        assertThat(actual.get(0).getQuestion().getText()).isEqualTo(QUESTION_TEXT);
        verify(studentQuizDetailRepository, times(2)).findFirstByStudentQuizId(STUDENT_QUIZ_ID);
        verify(studentQuizDetailRepository).save(any(StudentQuizDetail.class));
        verify(studentQuestionService).findAllQuestionsFromMultipleQuestionBank(true,
                Collections.singletonList(questionBank), studentQuiz.getQuiz().getQuestionCount());
        verify(studentQuestionService)
                .createStudentQuestionsFromQuestionList(Collections.singletonList(question), studentQuizDetail);
    }

    @Test
    public void answerStudentQuiz() {
        StudentQuizDetail actual = studentQuizDetailService
                .answerStudentQuiz(STUDENT_QUIZ_ID, Collections.singletonList(studentQuestion));
        assertThat(actual.getPoint()).isEqualTo(100);
        verify(studentQuizDetailRepository).findFirstByStudentQuizId(STUDENT_QUIZ_ID);
        verify(studentQuestionService).postAnswerForAllStudentQuestion(Collections.singletonList(studentQuestion));
    }

    @Test
    public void createStudentQuizDetail() {
        StudentQuizDetail actual = studentQuizDetailService.createStudentQuizDetail(studentQuiz,
                Collections.singletonList(studentQuestion));
        assertThat(actual.getStudentQuiz().getId()).isEqualTo(STUDENT_QUIZ_ID);
        verify(studentQuestionService).createStudentQuestionsByStudentQuizDetail(any(StudentQuizDetail.class),
                eq(Collections.singletonList(studentQuestion)));
        verify(studentQuizDetailRepository).save(any(StudentQuizDetail.class));
    }

    @Test
    public void createStudentQuizDetailNoQuestions() {
        StudentQuizDetail actual = studentQuizDetailService.createStudentQuizDetail(studentQuiz, null);
        assertThat(actual.getStudentQuiz().getId()).isEqualTo(STUDENT_QUIZ_ID);
        verify(studentQuizDetailRepository).save(any(StudentQuizDetail.class));
    }

    @Test
    public void validateQuestionsAndCreateStudentQuestions() {
        List<StudentQuestion> actual = studentQuizDetailService.validateQuestionsAndCreateStudentQuestions(studentQuizDetail,
                Collections.singletonList(studentQuestion));
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getOption().getLabel()).isEqualTo(OPTION_LABEL);
        assertThat(actual.get(0).getQuestion().getText()).isEqualTo(QUESTION_TEXT);
        verify(studentQuestionService).createStudentQuestionsByStudentQuizDetail(studentQuizDetail,
                Collections.singletonList(studentQuestion));
    }

    @Test
    public void validateQuestionsAndCreateStudentQuestionsEmptyQuestionList() {
        List<StudentQuestion> actual = studentQuizDetailService.validateQuestionsAndCreateStudentQuestions(studentQuizDetail,
                Collections.emptyList());
        assertThat(actual.size()).isEqualTo(0);
    }

    @Test
    public void deleteByStudentQuiz() {
        studentQuizDetailService.deleteByStudentQuiz(studentQuiz);
        studentQuizDetail.setDeleted(true);
        verify(studentQuizDetailRepository).findFirstByStudentQuizId(STUDENT_QUIZ_ID);
        verify(studentQuizDetailRepository).save(studentQuizDetail);
        verify(studentQuestionService).deleteAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID);
    }
}