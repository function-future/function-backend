package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.OptionWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionResponseMapperTest {

    private static final String OPTION_LABEL = "option-label";

    private Option option;
    private OptionWebResponse optionWebResponse;

    @Before
    public void setUp() throws Exception {
        option = Option
                .builder()
                .label(OPTION_LABEL)
                .build();

        optionWebResponse = OptionWebResponse
                .builder()
                .label(OPTION_LABEL)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void toOptionWebResponse() {
        DataResponse<OptionWebResponse> actual = OptionResponseMapper.toOptionWebResponse(option);

        assertThat(actual.getData().getLabel()).isEqualTo(optionWebResponse.getLabel());
        assertThat(actual.getCode()).isEqualTo(200);
    }

    @Test
    public void toOptionWebResponseCreated() {
        DataResponse<OptionWebResponse> actual = OptionResponseMapper.toOptionWebResponse(HttpStatus.CREATED, option);
        assertThat(actual.getData().getLabel()).isEqualTo(optionWebResponse.getLabel());
        assertThat(actual.getCode()).isEqualTo(201);
    }

    @Test
    public void toListOfOptionWebResponse() {
        List<OptionWebResponse> actual = OptionResponseMapper.toListOfOptionWebResponse(Collections.singletonList(option));
        assertThat(actual.get(0).getLabel()).isEqualTo(optionWebResponse.getLabel());
    }
}