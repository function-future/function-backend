package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.StudentQuestionWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentQuestionRequestMapperTest {

    private static final Integer NUMBER = 1;
    private static final String OPTION_ID = "option-id";

    private Option option;
    private StudentQuestion studentQuestion;
    private StudentQuestionWebRequest studentQuestionWebRequest;

    @Mock
    private RequestValidator objectValidator;

    @Mock
    private OptionRequestMapper optionRequestMapper;

    @InjectMocks
    private StudentQuestionRequestMapper requestMapper;

    @Before
    public void setUp() throws Exception {

        option = Option.builder().id(OPTION_ID).build();
        studentQuestion = StudentQuestion.builder().number(NUMBER).option(option).build();

        studentQuestionWebRequest = StudentQuestionWebRequest.builder().optionId(OPTION_ID).number(NUMBER).build();

        when(objectValidator.validate(studentQuestionWebRequest)).thenReturn(studentQuestionWebRequest);
        when(optionRequestMapper.toOptionFromOptionId(OPTION_ID)).thenReturn(option);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(objectValidator, optionRequestMapper);
    }

    @Test
    public void toStudentQuestion() {
        StudentQuestion actual = requestMapper.toStudentQuestion(studentQuestionWebRequest);

        assertThat(actual.getNumber()).isEqualTo(NUMBER);
        assertThat(actual.getOption().getId()).isEqualTo(OPTION_ID);
        verify(objectValidator).validate(studentQuestionWebRequest);
        verify(optionRequestMapper).toOptionFromOptionId(OPTION_ID);
    }

    @Test
    public void toStudentQuestionList() {
        List<StudentQuestion> actual = requestMapper.toStudentQuestionList(Collections.singletonList(studentQuestionWebRequest));
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getNumber()).isEqualTo(NUMBER);
        assertThat(actual.get(0).getOption().getId()).isEqualTo(OPTION_ID);
        verify(objectValidator).validate(studentQuestionWebRequest);
        verify(optionRequestMapper).toOptionFromOptionId(OPTION_ID);

    }
}