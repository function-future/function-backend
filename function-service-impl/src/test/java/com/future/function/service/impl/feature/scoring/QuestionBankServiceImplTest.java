package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.repository.feature.scoring.QuestionBankRepository;
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
public class QuestionBankServiceImplTest {

  private static final String QUESTIONBANK_ID = "random-id";
  private static final String QUESTIONBANK_DESCRIPTION = "questionbank-description";

  private static final int PAGE = 0;
  private static final int TOTAL = 10;

  private Pageable pageable;
  private QuestionBank questionBank;
  private List<QuestionBank> questionBankList;
  private Page<QuestionBank> questionBankPage;

  @InjectMocks
  private QuestionBankServiceImpl questionBankService;

  @Mock
  private QuestionBankRepository questionBankRepository;

  @Before
  public void setUp() throws Exception {
    questionBank = QuestionBank
            .builder()
            .id(QUESTIONBANK_ID)
            .description(QUESTIONBANK_DESCRIPTION)
            .build();

    pageable = new PageRequest(PAGE, TOTAL);
    questionBankList = Collections.singletonList(questionBank);
    questionBankPage = new PageImpl<>(questionBankList, pageable, TOTAL);

    when(questionBankRepository.findAll(pageable))
            .thenReturn(questionBankPage);
    when(questionBankRepository.findByIdAndDeletedFalse(QUESTIONBANK_ID))
            .thenReturn(Optional.of(questionBank));
    when(questionBankRepository.save(questionBank))
            .thenReturn(questionBank);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(questionBankRepository);
  }

  @Test
  public void testFindByIdSuccess() {
    QuestionBank actual = questionBankService.findById(QUESTIONBANK_ID);

    assertThat(actual).isEqualTo(questionBank);
    verify(questionBankRepository).findByIdAndDeletedFalse(QUESTIONBANK_ID);
  }

  @Test
  public void testFindByIdNullFail() {
    catchException(() -> questionBankService.findById(null));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void testFindByIdEmptyFail() {
    catchException(() -> questionBankService.findById(""));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void testFindAllWithPageableFilterAndSearch() {
    Page<QuestionBank> actual = questionBankService.findAllByPageableFilterAndSearch(pageable, "", "");

    assertThat(actual.getContent()).isEqualTo(questionBankList);
    assertThat(actual.getTotalElements()).isEqualTo(questionBankPage.getTotalElements());
    verify(questionBankRepository).findAll(pageable);
  }

  @Test
  public void testCreateQuestionBank() {
    QuestionBank actual = questionBankService.createQuestionBank(questionBank);

    assertThat(actual).isEqualTo(questionBank);
    verify(questionBankRepository).save(questionBank);
  }

  @Test
  public void testUpdateQuestionBank() {
    QuestionBank actual = questionBankService.updateQuestionBank(questionBank);

    assertThat(actual).isEqualTo(questionBank);
    verify(questionBankRepository).save(questionBank);
  }

  @Test
  public void testDeleteQuestionBank() {
    questionBank.setDeleted(true);
    questionBankService.deleteById(QUESTIONBANK_ID);
    verify(questionBankRepository).findByIdAndDeletedFalse(QUESTIONBANK_ID);
    verify(questionBankRepository).save(questionBank);
  }

  @Test
  public void testDeleteQuestionBankEmptyString() {
    catchException(() -> questionBankService.deleteById(""));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }
}