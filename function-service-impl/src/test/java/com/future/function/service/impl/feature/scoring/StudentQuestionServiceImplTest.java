package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuestionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentQuestionServiceImplTest {

    private static final String STUDENT_QUIZ_DETAIL_ID = "student-quiz-detail-id";
    private static final String STUDENT_QUESTION_ID = "student-question-id";
    private static final String QUESTION_ID = "question-id";
    private static final String QUESTION_TEXT = "question-text";
    private static final String OPTION_ID = "option-id";
    private static final String OPTION_LABEL = "option-label";

    private StudentQuestion studentQuestion;
    private Question question;
    private Option option;
    private Sort sort;

    @InjectMocks
    private StudentQuestionServiceImpl studentQuestionService;

    @Mock
    private StudentQuestionRepository studentQuestionRepository;

    @Before
    public void setUp() throws Exception {
        question = Question
                .builder()
                .id(QUESTION_ID)
                .text(QUESTION_TEXT)
                .build();

        option = Option
                .builder()
                .id(OPTION_ID)
                .label(OPTION_LABEL)
                .question(question)
                .build();

        studentQuestion = StudentQuestion
                .builder()
                .id(STUDENT_QUESTION_ID)
                .number(1)
                .question(question)
                .option(option)
                .studentQuizDetail(StudentQuizDetail.builder().id(STUDENT_QUIZ_DETAIL_ID).build())
                .build();

        sort = new Sort(Sort.Direction.ASC, "number");

        when(studentQuestionRepository.findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID, sort))
                .thenReturn(Collections.singletonList(studentQuestion));
        when(studentQuestionRepository.save(studentQuestion)).thenReturn(studentQuestion);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(studentQuestionRepository);
    }

    @Test
    public void findAllByStudentQuizDetailId() {
        List<StudentQuestion> actual = studentQuestionService.findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID);
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getQuestion().getText()).isEqualTo(QUESTION_TEXT);
        assertThat(actual.get(0).getOption().getLabel()).isEqualTo(OPTION_LABEL);
        verify(studentQuestionRepository).findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID, sort);
    }

    @Test
    public void postAnswerForAllStudentQuestion() {
        Integer actual = studentQuestionService
                .postAnswerForAllStudentQuestion(Collections.singletonList(studentQuestion));
        assertThat(actual).isEqualTo(100);
        verify(studentQuestionRepository).findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID, sort);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void postAnswerForAllStudentQuestionNotSameStudentQuizDetailId() {
        StudentQuestion anotherOne = StudentQuestion
                .builder()
                .number(2)
                .id("abc")
                .question(question)
                .option(option)
                .studentQuizDetail(StudentQuizDetail.builder().id("abc").build())
                .build();

        studentQuestionService
                .postAnswerForAllStudentQuestion(Arrays.asList(studentQuestion, anotherOne));
    }

    @Test
    public void createStudentQuestionsByStudentQuizDetail() {
        List<StudentQuestion> actual = studentQuestionService.createStudentQuestionsByStudentQuizDetail(
                StudentQuizDetail.builder().id(STUDENT_QUIZ_DETAIL_ID).build(), Collections.singletonList(studentQuestion));
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getQuestion().getText()).isEqualTo(QUESTION_TEXT);
        assertThat(actual.get(0).getOption().getLabel()).isEqualTo(OPTION_LABEL);
        verify(studentQuestionRepository).save(studentQuestion);
    }

    @Test
    public void deleteAllByStudentQuizDetailId() {
        studentQuestion.setDeleted(true);
        studentQuestionService.deleteAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID);
        verify(studentQuestionRepository).findAllByStudentQuizDetailId(STUDENT_QUIZ_DETAIL_ID, sort);
        verify(studentQuestionRepository).save(studentQuestion);
    }
}