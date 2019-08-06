package com.future.function.web.mapper.response.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportResponseMapperTest {

  private static final String ID = "id";
  private static final String NAME = "final-judge";
  private static final String DESCRIPTION = "final description";
    private static final String BATCH_CODE = "batch-code";
  private static final String STUDENT_ID = "student-id";
  private static final String STUDENT_NAME = "student-name";
  private static final String STUDENT_PHONE = "student-phone";
  private static final String STUDENT_AVATAR = "student-avatar";
  private static final String STUDENT_AVATAR_ID = "student-avatar-id";
  private static final String STUDENT_ADDRESS = "student-address";
  private static final String STUDENT_EMAIL = "student-email";
  private static final String STUDENT_UNIVERSITY = "student-university";
  private static final Long CREATED_AT = new Date().getTime();

  private Report report;
    private Batch batch;
  private ReportWebResponse response;
  private Pageable pageable;
  private Page<Report> reportPage;
  private User user;
  private UserWebResponse userWebResponse;

  @Before
  public void setUp() throws Exception {

      batch = Batch.builder().code(BATCH_CODE).build();

      userWebResponse = UserWebResponse.builder()
          .id(STUDENT_ID)
          .name(STUDENT_NAME)
          .role(Role.STUDENT.name())
          .address(STUDENT_ADDRESS)
          .phone(STUDENT_PHONE)
          .avatar(STUDENT_AVATAR)
              .avatarId(STUDENT_AVATAR_ID)
          .batch(BatchWebResponse.builder().code(BATCH_CODE).build())
          .email(STUDENT_EMAIL)
          .university(STUDENT_UNIVERSITY).build();

      user = User.builder()
          .id(STUDENT_ID)
          .name(STUDENT_NAME)
          .role(Role.STUDENT)
          .address(STUDENT_ADDRESS)
          .phone(STUDENT_PHONE)
              .pictureV2(FileV2.builder().id(STUDENT_AVATAR_ID).fileUrl(STUDENT_AVATAR).build())
          .batch(batch)
          .email(STUDENT_EMAIL)
          .university(STUDENT_UNIVERSITY).build();

    report = Report
        .builder()
            .id(ID)
        .title(NAME)
            .batch(batch)
        .description(DESCRIPTION)
        .students(Collections.singletonList(user))
        .build();

    report.setCreatedAt(CREATED_AT);

    pageable = new PageRequest(0, 10);

    reportPage = new PageImpl<>(Collections.singletonList(report), pageable, 1);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void toDataReportWebResponse() {
    DataResponse<ReportWebResponse> actual = ReportResponseMapper.toDataReportWebResponse(report);
    assertThat(actual.getData().getId()).isEqualTo(ID);
    assertThat(actual.getData().getName()).isEqualTo(NAME);
    assertThat(actual.getData().getDescription()).isEqualTo(DESCRIPTION);
      assertThat(actual.getData().getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData().getStudentCount()).isEqualTo(1);
  }

  @Test
  public void toDataReportWebResponseCreated() {
    DataResponse<ReportWebResponse> actual = ReportResponseMapper.toDataReportWebResponse(HttpStatus.CREATED, report);
    assertThat(actual.getData().getId()).isEqualTo(ID);
    assertThat(actual.getData().getName()).isEqualTo(NAME);
    assertThat(actual.getData().getDescription()).isEqualTo(DESCRIPTION);
      assertThat(actual.getData().getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData().getStudentCount()).isEqualTo(1);
    assertThat(actual.getCode()).isEqualTo(201);
  }

  @Test
  public void toPagingReportWebResponse() {
    PagingResponse<ReportWebResponse> actual = ReportResponseMapper.toPagingReportWebResponse(reportPage);
    assertThat(actual.getData().get(0).getName()).isEqualTo(NAME);
    assertThat(actual.getData().get(0).getDescription()).isEqualTo(DESCRIPTION);
      assertThat(actual.getData().get(0).getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.getData().get(0).getStudentCount()).isEqualTo(1);
    assertThat(actual.getPaging().getTotalRecords()).isEqualTo(1);
  }
}
