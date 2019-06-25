package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.OptionWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OptionRequestMapperTest {

    private static final String OPTION_LABEL = "option-label";
    private static final boolean CORRECT = true;

    private Option option;
    private OptionWebRequest optionWebRequest;

    @InjectMocks
    private OptionRequestMapper requestMapper;

    @Mock
    private RequestValidator validator;

    @Before
    public void setUp() throws Exception {

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

    @Test
    public void testToOptionWithOptionId() {
        Option actual = requestMapper.toOptionFromOptionId("id");
        assertThat(actual.getId()).isEqualTo("id");
    }

    @Test
    public void testToOptionWithOptionIdBlank() {
        catchException(() -> requestMapper.toOptionFromOptionId(""));
        assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
    }

    @Test
    public void testToOptionWithOptionIdNull() {
        catchException(() -> requestMapper.toOptionFromOptionId(null));
        assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
    }
}