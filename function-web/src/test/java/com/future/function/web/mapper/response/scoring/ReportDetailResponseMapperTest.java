package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ReportDetailResponseMapperTest {

  private static final String REPORT_DETAIL_ID = "report-detail-id";

  private static final String STUDENT_NAME = "student-name";

  private static final String BATCH_CODE = "batch-code";

  private static final String UNIVERSITY = "university";

  private static final String TITLE = "title";

  private static final String TYPE = "type";

  private static final String FILE_URL = "file-url";

  private static final String URL_PREFIX = "urlPrefix";

  private static final int POINT = 100;

  private static final int TOTAL_POINT = 100;

  private ReportDetail reportDetail;

  private FileV2 fileV2;

  private User student;

  private Batch batch;

  private SummaryVO summaryVO;

  private StudentSummaryVO studentSummaryVO;

  @Before
  public void setUp() throws Exception {

    summaryVO = SummaryVO.builder()
      .title(TITLE)
      .type(TYPE)
      .point(POINT)
      .build();

    fileV2 = FileV2.builder()
      .fileUrl(FILE_URL)
      .build();

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();
    student = User.builder()
      .name(STUDENT_NAME)
      .batch(batch)
      .university(UNIVERSITY)
      .pictureV2(fileV2)
      .build();
    reportDetail = ReportDetail.builder()
      .id(REPORT_DETAIL_ID)
      .user(student)
      .point(0)
      .build();
    studentSummaryVO = StudentSummaryVO.builder()
      .studentName(STUDENT_NAME)
      .batchCode(BATCH_CODE)
      .university(UNIVERSITY)
      .avatar(FILE_URL)
      .scores(new PageImpl<>(Collections.singletonList(summaryVO)))
      .totalPoint(TOTAL_POINT)
      .build();
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void toDataReportDetailWebResponse() {

    DataResponse<ReportDetailWebResponse> actual =
      ReportDetailResponseMapper.toDataReportDetailWebResponse(
        studentSummaryVO, URL_PREFIX);
    assertThat(actual.getData()
                 .getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData()
                 .getStudentName()).isEqualTo(STUDENT_NAME);
    assertThat(actual.getData()
                 .getScores()
                 .get(0)
                 .getTitle()).isEqualTo(TITLE);
    assertThat(actual.getData()
                 .getAvatar()).isEqualTo(URL_PREFIX + FILE_URL);
  }

  @Test
  public void toDataListReportDetailWebResponse() {

    DataResponse<List<ReportDetailWebResponse>> actual =
      ReportDetailResponseMapper.toDataListReportDetailWebResponse(
        Collections.singletonList(studentSummaryVO), URL_PREFIX);
    assertThat(actual.getData()
                 .get(0)
                 .getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData()
                 .get(0)
                 .getStudentName()).isEqualTo(STUDENT_NAME);
    assertThat(actual.getData()
                 .get(0)
                 .getUniversity()).isEqualTo(UNIVERSITY);
    assertThat(actual.getData()
                 .get(0)
                 .getAvatar()).isEqualTo(URL_PREFIX + FILE_URL);
    assertThat(actual.getData()
                 .get(0)
                 .getScores()
                 .get(0)
                 .getTitle()).isEqualTo(TITLE);
  }

  @Test
  public void toDataListReportDetailWebResponseFromReportDetail() {

    DataResponse<List<ReportDetailWebResponse>> actual =
      ReportDetailResponseMapper.toDataListReportDetailWebResponseFromReportDetail(
        HttpStatus.CREATED, Collections.singletonList(reportDetail),
        URL_PREFIX
      );
    assertThat(actual.getData()
                 .get(0)
                 .getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData()
                 .get(0)
                 .getStudentName()).isEqualTo(STUDENT_NAME);
    assertThat(actual.getData()
                 .get(0)
                 .getUniversity()).isEqualTo(UNIVERSITY);
    assertThat(actual.getData()
                 .get(0)
                 .getAvatar()).isEqualTo(URL_PREFIX + FILE_URL);
  }

  @Test
  public void toDataListReportDetailWebResponseWithEmptyAvatar() {

    studentSummaryVO.setAvatar("");
    DataResponse<List<ReportDetailWebResponse>> actual =
      ReportDetailResponseMapper.toDataListReportDetailWebResponse(
        Collections.singletonList(studentSummaryVO), URL_PREFIX);
    assertThat(actual.getData()
                 .get(0)
                 .getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData()
                 .get(0)
                 .getStudentName()).isEqualTo(STUDENT_NAME);
    assertThat(actual.getData()
                 .get(0)
                 .getUniversity()).isEqualTo(UNIVERSITY);
    assertThat(actual.getData()
                 .get(0)
                 .getAvatar()).isNull();
    assertThat(actual.getData()
                 .get(0)
                 .getScores()
                 .get(0)
                 .getTitle()).isEqualTo(TITLE);
  }

}
