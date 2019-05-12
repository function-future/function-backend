package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.web.model.request.scoring.OptionWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OptionRequestMapperTest {

    private static final String OPTION_LABEL = "option-label";
    private static final boolean CORRECT = true;

    private Option option;
    private OptionWebRequest optionWebRequest;

    @InjectMocks
    private OptionRequestMapper requestMapper;

    @Mock
    private ObjectValidator validator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        optionWebRequest = OptionWebRequest
                .builder()
                .label(OPTION_LABEL)
                .correct(CORRECT)
                .build();

        option = Option
                .builder()
                .label(OPTION_LABEL)
                .correct(CORRECT)
                .build();

        when(validator.validate(optionWebRequest)).thenReturn(optionWebRequest);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(validator);
    }

    @Test
    public void testToOption() {
        Option actual = requestMapper.toOption(optionWebRequest);

        assertThat(actual.getLabel()).isEqualTo(option.getLabel());
        assertThat(actual.isCorrect()).isEqualTo(option.isCorrect());

        verify(validator).validate(optionWebRequest);
    }

    @Test
    public void testToOptionCorrectFalse() {
        optionWebRequest.setCorrect(false);
        option.setCorrect(false);
        Option actual = requestMapper.toOption(optionWebRequest);

        assertThat(actual.getLabel()).isEqualTo(option.getLabel());
        assertThat(actual.isCorrect()).isEqualTo(option.isCorrect());

        verify(validator).validate(optionWebRequest);
    }

    @Test
    public void testToOptionCorrectNull() {
        optionWebRequest.setCorrect(null);
        option.setCorrect(false);
        Option actual = requestMapper.toOption(optionWebRequest);

        assertThat(actual.getLabel()).isEqualTo(option.getLabel());
        assertThat(actual.isCorrect()).isEqualTo(option.isCorrect());

        verify(validator).validate(optionWebRequest);
    }

    @Test
    public void testToOptionNull() {
        catchException(() -> requestMapper.toOption(null));

        assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
    }
}