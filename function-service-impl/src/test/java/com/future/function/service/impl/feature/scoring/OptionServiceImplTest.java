package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.repository.feature.scoring.OptionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OptionServiceImplTest {

    private static final String OPTION_ID = "id";
    private static final String OPTION_LABEL = "label";

    private static final String QUESTION_ID = "id";

    private Question question;
    private Option option;

    @InjectMocks
    private OptionServiceImpl optionService;

    @Mock
    private OptionRepository optionRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        question = Question.builder()
                .id(QUESTION_ID)
                .build();

        option = Option
                .builder()
                .id(OPTION_ID)
                .label(OPTION_LABEL)
                .correct(true)
                .question(question)
                .build();

        when(optionRepository.findByIdAndDeletedFalse(OPTION_ID)).thenReturn(Optional.of(option));
        when(optionRepository.save(option)).thenReturn(option);
        when(optionRepository.findAllByQuestionId(QUESTION_ID)).thenReturn(Collections.singletonList(option));
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(optionRepository);
    }

    @Test
    public void getOptionListByQuestionId() {
        List<Option> actual = optionService.getOptionListByQuestionId(QUESTION_ID);
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(option);
        verify(optionRepository).findAllByQuestionId(QUESTION_ID);
    }

    @Test
    public void getOptionListByQuestionIdNull() {
        catchException(() -> optionService.getOptionListByQuestionId(null));

        assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    public void findById() {
        Option actual = optionService.findById(OPTION_ID);
        assertThat(actual).isEqualTo(option);
        verify(optionRepository).findByIdAndDeletedFalse(OPTION_ID);
    }

    @Test
    public void findByIdNull() {
        catchException(() -> optionService.findById(null));
        assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    public void createOption() {
        Option actual = optionService.createOption(option);
        assertThat(actual).isEqualTo(option);
        verify(optionRepository).save(option);
    }

    @Test
    public void updateOption() {
        Option actual = optionService.updateOption(option);
        assertThat(actual).isEqualTo(option);
        verify(optionRepository).save(option);
    }

    @Test
    public void deleteById() {
        optionService.deleteById(OPTION_ID);
        verify(optionRepository).findByIdAndDeletedFalse(OPTION_ID);
        option.setDeleted(true);
        verify(optionRepository).save(option);
    }

    @Test
    public void deleteByIdNull() {
        optionService.deleteById(null);
    }
}