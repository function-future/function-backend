package com.future.function.web.mapper.response.scoring;

import com.future.function.model.dto.scoring.StudentSummaryDTO;
import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ReportDetailResponseMapperTest {

    private static final String REPORT_DETAIL_ID = "report-detail-id";
    private static final String STUDENT_NAME = "student-name";
    private static final String BATCH_CODE = "batch-code";
  private static final String UNIVERSITY = "university";
    private static final String TITLE = "title";
    private static final String TYPE = "type";
    private static final int POINT = 100;

    private ReportDetail reportDetail;
    private User student;
    private Batch batch;
    private SummaryDTO summaryDTO;
  private StudentSummaryDTO studentSummaryDTO;

    @Before
    public void setUp() throws Exception {
        summaryDTO = SummaryDTO.builder()
                .title(TITLE)
                .type(TYPE)
                .point(POINT)
                .build();

        batch = Batch.builder().code(BATCH_CODE).build();
      student = User.builder().name(STUDENT_NAME).batch(batch).university(UNIVERSITY).build();
        reportDetail = ReportDetail.builder().id(REPORT_DETAIL_ID).user(student).point(0).build();
      studentSummaryDTO = StudentSummaryDTO.builder().studentName(STUDENT_NAME).batchCode(BATCH_CODE)
          .university(UNIVERSITY).scores(Collections.singletonList(summaryDTO)).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void toDataReportDetailWebResponse() {
        DataResponse<ReportDetailWebResponse> actual = ReportDetailResponseMapper.toDataReportDetailWebResponse(
            studentSummaryDTO);
        assertThat(actual.getData().getBatchCode()).isEqualTo(BATCH_CODE);
        assertThat(actual.getData().getStudentName()).isEqualTo(STUDENT_NAME);
        assertThat(actual.getData().getScores().get(0).getTitle()).isEqualTo(TITLE);
    }

    @Test
    public void toDataListReportDetailWebResponse() {
        DataResponse<List<ReportDetailWebResponse>> actual = ReportDetailResponseMapper.toDataListReportDetailWebResponse(
            Collections.singletonList(studentSummaryDTO));
        assertThat(actual.getData().get(0).getBatchCode()).isEqualTo(BATCH_CODE);
        assertThat(actual.getData().get(0).getStudentName()).isEqualTo(STUDENT_NAME);
        assertThat(actual.getData().get(0).getScores().get(0).getTitle()).isEqualTo(TITLE);
    }
}
