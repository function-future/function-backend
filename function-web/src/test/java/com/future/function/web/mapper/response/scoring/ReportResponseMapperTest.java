package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportResponseMapperTest {

  private static final String NAME = "final-judge";
  private static final String DESCRIPTION = "final description";
  private static final long USED_AT = 1561607367;
  private static final String STUDENT_ID = "student-id";

  private Report report;
  private ReportWebResponse response;
  private Pageable pageable;
  private Page<Report> reportPage;
  private List<String> studentIds;

  @Before
  public void setUp() throws Exception {

    studentIds = Collections.singletonList(STUDENT_ID);

    report = Report
        .builder()
        .title(NAME)
        .description(DESCRIPTION)
        .usedAt(Instant.ofEpochSecond(USED_AT).atZone(ZoneId.systemDefault()).toLocalDate())
        .studentIds(studentIds)
        .build();

    pageable = new PageRequest(0, 10);

    reportPage = new PageImpl<>(Collections.singletonList(report), pageable, 1);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void toDataReportWebResponse() {
    DataResponse<ReportWebResponse> actual = ReportResponseMapper.toDataReportWebResponse(report);
    assertThat(actual.getData().getTitle()).isEqualTo(NAME);
    assertThat(actual.getData().getDescription()).isEqualTo(DESCRIPTION);
    assertThat(actual.getData().getStudentCount()).isEqualTo(1);
  }

  @Test
  public void toPagingReportWebResponse() {
    PagingResponse<ReportWebResponse> actual = ReportResponseMapper.toPagingReportWebResponse(reportPage);
    assertThat(actual.getData().get(0).getTitle()).isEqualTo(NAME);
    assertThat(actual.getData().get(0).getDescription()).isEqualTo(DESCRIPTION);
    assertThat(actual.getData().get(0).getStudentCount()).isEqualTo(1);
    assertThat(actual.getPaging().getTotalRecords()).isEqualTo(1);
  }
}
