package com.future.function.web.mapper.response.scoring;

import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.SummaryResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ScoreSummaryResponseMapperTest {

    private static final String TITLE = "title";
    private static final String TYPE = "type";
    private static final int POINT = 100;

    private SummaryDTO summaryDTO;

    @Before
    public void setUp() throws Exception {
        summaryDTO = SummaryDTO.builder()
                .title(TITLE)
                .type(TYPE)
                .point(POINT)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void toDataSummaryResponse() {
        DataResponse<SummaryResponse> actual = ScoreSummaryResponseMapper.toDataSummaryResponse(summaryDTO);
        assertThat(actual.getData().getTitle()).isEqualTo(TITLE);
        assertThat(actual.getData().getType()).isEqualTo(TYPE);
        assertThat(actual.getData().getPoint()).isEqualTo(POINT);
    }

    @Test
    public void toDataListSummaryResponse() {
        DataResponse<List<SummaryResponse>> actual = ScoreSummaryResponseMapper
                .toDataListSummaryResponse(Collections.singletonList(summaryDTO));
        assertThat(actual.getData().get(0).getTitle()).isEqualTo(TITLE);
        assertThat(actual.getData().get(0).getType()).isEqualTo(TYPE);
        assertThat(actual.getData().get(0).getPoint()).isEqualTo(POINT);
    }
}