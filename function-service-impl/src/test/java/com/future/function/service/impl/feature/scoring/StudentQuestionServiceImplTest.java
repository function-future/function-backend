package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuestionRepository;
import com.future.function.service.api.feature.scoring.QuestionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentQuestionServiceImplTest {

  private static final String STUDENT_QUIZ_DETAIL_ID = "student-quiz-detail-id";

  private static final String STUDENT_QUESTION_ID = "student-question-id";

  private static final String QUESTION_ID = "question-id";

  private static final String QUESTION_TEXT = "question-text";

  private static final String OPTION_ID = "option-id";

  private static final String OPTION_LABEL = "option-label";

  private static final String QUESTION_BANK_ID = "question-bank-id";

  private static final int QUESTION_COUNT = 10;

  private QuestionBank questionBank;

  private StudentQuizDetail studentQuizDetail;

  private StudentQuestion studentQuestion;

  private Question question;

  private Option option;

  private Sort sort;

  @InjectMocks
  private StudentQuestionServiceImpl studentQuestionService;

  @Mock
  private StudentQuestionRepository studentQuestionRepository;

  @Mock
  private QuestionService questionService;

  @Before
  public void setUp() throws Exception {

    studentQuizDetail = StudentQuizDetail.builder()
      .id(STUDENT_QUIZ_DETAIL_ID)
      .build();

    questionBank = QuestionBank.builder()
      .id(QUESTION_BANK_ID)
      .build();

    question = Question.builder()
      .id(QUESTION_ID)
      .questionBank(questionBank)
      .label(QUESTION_TEXT)
      .build();

    option = Option.builder()
      .id(OPTION_ID)
      .label(OPTION_LABEL)
      .correct(true)
      .build();

    question.setOptions(Collections.singletonList(option));

    studentQuestion = StudentQuestion.builder()
      .id(STUDENT_QUESTION_ID)
      .number(1)
      .question(question)
      .option(option)
      .studentQuizDetail(studentQuizDetail)
      .build();

    sort = new Sort(Sort.DEFAULT_DIRECTION, "number");

    when(
      studentQuestionRepository.findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(
        STUDENT_QUIZ_DETAIL_ID)).thenReturn(
      Collections.singletonList(studentQuestion));
    when(studentQuestionRepository.save(studentQuestion)).thenReturn(
      studentQuestion);
    when(studentQuestionRepository.save(any(StudentQuestion.class))).thenReturn(
      studentQuestion);
    when(questionService.findAllByMultipleQuestionBankId(
      Collections.singletonList(QUESTION_BANK_ID))).thenReturn(
      Collections.singletonList(question));
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(studentQuestionRepository);
  }

  @Test
  public void findAllByStudentQuizDetailId() {

    List<StudentQuestion> actual =
      studentQuestionService.findAllByStudentQuizDetailId(
        STUDENT_QUIZ_DETAIL_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getQuestion()
                 .getLabel()).isEqualTo(QUESTION_TEXT);
    assertThat(actual.get(0)
                 .getOption()
                 .getLabel()).isEqualTo(OPTION_LABEL);
    verify(
      studentQuestionRepository).findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(
      STUDENT_QUIZ_DETAIL_ID);
  }

  @Test
  public void findAllQuestionsFromMultipleQuestionBanks() {

    List<Question> actual =
      studentQuestionService.findAllRandomQuestionsFromMultipleQuestionBank(
        Collections.singletonList(questionBank), QUESTION_COUNT);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getLabel()).isEqualTo(QUESTION_TEXT);
    verify(questionService).findAllByMultipleQuestionBankId(
      Collections.singletonList(QUESTION_BANK_ID));
  }

  @Test
  public void findAllQuestionsFromMultipleQuestionBanksNotRandom() {

    List<Question> actual =
      studentQuestionService.findAllRandomQuestionsFromMultipleQuestionBank(
        Collections.singletonList(questionBank), 0);
    assertThat(actual.size()).isEqualTo(0);
    verify(questionService).findAllByMultipleQuestionBankId(
      Collections.singletonList(QUESTION_BANK_ID));
  }

  @Test
  public void findAllQuestionsFromMultipleQuestionBanksEmptyQuestionBankList() {

    List<Question> actual =
      studentQuestionService.findAllRandomQuestionsFromMultipleQuestionBank(
        Collections.emptyList(), QUESTION_COUNT);
    assertThat(actual.size()).isEqualTo(0);
  }

  @Test
  public void createStudentQuestionsFromQuestionList() {

    List<StudentQuestion> actual =
      studentQuestionService.createStudentQuestionsFromQuestionList(
        Collections.singletonList(question), studentQuizDetail);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getNumber()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getQuestion()
                 .getLabel()).isEqualTo(QUESTION_TEXT);
    verify(studentQuestionRepository).save(any(StudentQuestion.class));
  }

  @Test
  public void createStudentQuestionsFromQuestionListNoCorrectOption() {

    question.getOptions()
      .get(0)
      .setCorrect(false);
    catchException(
      () -> studentQuestionService.createStudentQuestionsFromQuestionList(
        Collections.singletonList(question), studentQuizDetail));
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void postAnswerForAllStudentQuestion() {

    Integer actual = studentQuestionService.postAnswerForAllStudentQuestion(
      Collections.singletonList(studentQuestion), STUDENT_QUIZ_DETAIL_ID);
    assertThat(actual).isEqualTo(100);
    verify(
      studentQuestionRepository).findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(
      STUDENT_QUIZ_DETAIL_ID);
    verify(studentQuestionRepository).save(studentQuestion);
  }

  @Test
  public void createStudentQuestionsByStudentQuizDetail() {

    List<StudentQuestion> actual =
      studentQuestionService.createStudentQuestionsByStudentQuizDetail(
        StudentQuizDetail.builder()
          .id(STUDENT_QUIZ_DETAIL_ID)
          .build(), Collections.singletonList(studentQuestion));
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getQuestion()
                 .getLabel()).isEqualTo(QUESTION_TEXT);
    assertThat(actual.get(0)
                 .getOption()
                 .getLabel()).isEqualTo(OPTION_LABEL);
    verify(studentQuestionRepository).save(studentQuestion);
  }

  @Test
  public void deleteAllByStudentQuizDetailId() {

    studentQuestion.setDeleted(true);
    studentQuestionService.deleteAllByStudentQuizDetailId(
      STUDENT_QUIZ_DETAIL_ID);
    verify(
      studentQuestionRepository).findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc(
      STUDENT_QUIZ_DETAIL_ID);
    verify(studentQuestionRepository).save(studentQuestion);
  }

}
