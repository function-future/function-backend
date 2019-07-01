package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.repository.feature.scoring.QuestionRepository;
import com.future.function.service.api.feature.scoring.OptionService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceImplTest {

  private static final String QUESTION_ID = "id";
  private static final String QUESTION_TEXT = "question";

  private static final String OPTION_ID = "option-id";
  private static final String OPTION_LABEL = "label";

  private static final String QUESTION_BANK_ID = "question-bank-id";

  private Question question;
  private QuestionBank questionBank;
  private Page<Question> questionPage;
  private Pageable pageable;
  private Option option;

  @InjectMocks
  private QuestionServiceImpl questionService;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private OptionService optionService;

  @Mock
  private QuestionBankService questionBankService;

  @Before
  public void setUp() throws Exception {

    questionBank = QuestionBank
        .builder()
        .id(QUESTION_BANK_ID)
        .build();

    question = Question
        .builder()
        .id(QUESTION_ID)
        .label(QUESTION_TEXT)
        .questionBank(questionBank)
        .build();

    option = Option
        .builder()
        .id(OPTION_ID)
        .correct(true)
        .label(OPTION_LABEL)
        .question(question)
        .build();

    question.setOptions(Collections.singletonList(option));

    pageable = new PageRequest(0, 10);

    questionPage = new PageImpl<>(Collections.singletonList(question), pageable, 1);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(questionRepository, optionService);
  }

  @Test
  public void findAllByQuestionBankIdTest() {
    when(questionBankService.findById(QUESTION_BANK_ID)).thenReturn(questionBank);

    when(questionRepository.findAllByQuestionBankId(QUESTION_BANK_ID, pageable)).thenReturn(questionPage);
    when(optionService.getOptionListByQuestionId(QUESTION_ID)).thenReturn(Collections.singletonList(option));

    when(questionRepository.findByIdAndDeletedFalse(QUESTION_ID)).thenReturn(Optional.of(question));
    Page<Question> actual = questionService.findAllByQuestionBankId(QUESTION_BANK_ID, pageable);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent()).isEqualTo(Collections.singletonList(question));

    verify(optionService).getOptionListByQuestionId(QUESTION_ID);
    verify(questionRepository).findAllByQuestionBankId(QUESTION_BANK_ID, pageable);
  }

  @Test
  public void findAllByQuestionBankIdNullTest() {
    catchException(() -> questionService.findAllByQuestionBankId(null, pageable));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void findAllQuestionByMultipleQuestionBankIdTest() {
    when(questionBankService.findById(QUESTION_BANK_ID)).thenReturn(questionBank);
    when(questionRepository.findAllByQuestionBankId(QUESTION_BANK_ID)).thenReturn(Collections.singletonList(question));
    when(optionService.getOptionListByQuestionId(QUESTION_ID)).thenReturn(Collections.singletonList(option));
    List<Question> actual = questionService.findAllByMultipleQuestionBankId(Collections.singletonList(QUESTION_BANK_ID));
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0).getLabel()).isEqualTo(QUESTION_TEXT);
    verify(questionRepository).findAllByQuestionBankId(QUESTION_BANK_ID);
    verify(optionService).getOptionListByQuestionId(QUESTION_ID);
  }

  @Test
  public void findAllQuestionByMultipleQuestionBankIdEmptyQuestionBankTest() {
    List<Question> actual = questionService.findAllByMultipleQuestionBankId(Collections.emptyList());
    assertThat(actual.size()).isEqualTo(0);
  }

  @Test
  public void findById() {
    when(questionRepository.findByIdAndDeletedFalse(QUESTION_ID)).thenReturn(Optional.of(question));
    when(optionService.getOptionListByQuestionId(QUESTION_ID)).thenReturn(Collections.singletonList(option));

    Question actual = questionService.findById(QUESTION_ID);

    assertThat(actual).isEqualTo(question);
    verify(questionRepository).findByIdAndDeletedFalse(QUESTION_ID);
    verify(optionService).getOptionListByQuestionId(QUESTION_ID);
  }

  @Test
  public void findByIdNull() {
    catchException(() -> questionService.findById(null));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void createQuestion() {
    when(questionRepository.save(question)).thenReturn(question);
    when(optionService.createOption(option)).thenReturn(option);

    Question actual = questionService.createQuestion(question, QUESTION_BANK_ID);
    assertThat(actual).isEqualTo(question);
    verify(questionRepository).save(question);
    verify(questionBankService).findById(QUESTION_BANK_ID);
    verify(optionService).createOption(option);
  }

  @Test
  public void updateQuestion() {

    when(questionRepository.findByIdAndDeletedFalse(QUESTION_ID)).thenReturn(Optional.of(question));
    when(questionRepository.save(question)).thenReturn(question);
    when(optionService.updateOption(option)).thenReturn(option);

    Question actual = questionService.updateQuestion(question, QUESTION_BANK_ID);

    assertThat(actual).isEqualTo(question);
    verify(questionRepository).findByIdAndDeletedFalse(question.getId());
    verify(questionRepository).save(question);
    verify(questionBankService).findById(QUESTION_BANK_ID);
    verify(optionService).updateOption(option);
  }

  @Test
  public void deleteById() {

    when(questionRepository.findByIdAndDeletedFalse(QUESTION_ID)).thenReturn(Optional.of(question));
    when(questionRepository.save(question)).thenReturn(question);
    when(optionService.getOptionListByQuestionId(QUESTION_ID)).thenReturn(Collections.singletonList(option));

    questionService.deleteById(QUESTION_ID);
    question.setDeleted(true);
    verify(questionRepository).findByIdAndDeletedFalse(question.getId());
    verify(optionService).getOptionListByQuestionId(QUESTION_ID);
    verify(optionService).deleteById(OPTION_ID);
    verify(questionRepository).save(question);
  }
}
