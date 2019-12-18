package com.future.function.web.mapper.response.scoring;

import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.SummaryWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.PageImpl;

import static org.assertj.core.api.Assertions.assertThat;


public class ScoreSummaryWebResponseMapperTest {

  private static final String TITLE = "title";

  private static final String TYPE = "type";

  private static final int POINT = 100;

  private SummaryVO summaryVO;

  @Before
  public void setUp() throws Exception {

    summaryVO = SummaryVO.builder()
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

    DataResponse<SummaryWebResponse> actual =
      ScoreSummaryResponseMapper.toDataSummaryResponse(summaryVO);
    assertThat(actual.getData()
                 .getTitle()).isEqualTo(TITLE);
    assertThat(actual.getData()
                 .getType()).isEqualTo(TYPE);
    assertThat(actual.getData()
                 .getPoint()).isEqualTo(POINT);
  }

  @Test
  public void toDataListSummaryResponse() {

    PagingResponse<SummaryWebResponse> actual =
      ScoreSummaryResponseMapper.toDataListSummaryResponse(
        new PageImpl<>(Collections.singletonList(summaryVO)));
    assertThat(actual.getData()
                 .get(0)
                 .getTitle()).isEqualTo(TITLE);
    assertThat(actual.getData()
                 .get(0)
                 .getType()).isEqualTo(TYPE);
    assertThat(actual.getData()
                 .get(0)
                 .getPoint()).isEqualTo(POINT);
  }

}
