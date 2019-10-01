package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ReportDetailScoreWebRequest;
import com.future.function.web.model.request.scoring.ScoreStudentWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ReportDetailRequestMapperTest {

  private static final String STUDENT_ID = "student-id";

  private static final String REPORT_ID = "report-id";

  private static final int SCORE = 100;

  private ScoreStudentWebRequest scoreStudentWebRequest;

  private ReportDetailScoreWebRequest reportDetailScoreWebRequest;

  @InjectMocks
  private ReportDetailRequestMapper requestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() throws Exception {

    scoreStudentWebRequest = ScoreStudentWebRequest.builder()
      .studentId(STUDENT_ID)
      .score(SCORE)
      .build();
    reportDetailScoreWebRequest = ReportDetailScoreWebRequest.builder()
      .scores(Collections.singletonList(scoreStudentWebRequest))
      .build();
    when(validator.validate(reportDetailScoreWebRequest)).thenReturn(
      reportDetailScoreWebRequest);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(validator);
  }

  @Test
  public void toReportDetailList() {

    List<ReportDetail> actual = requestMapper.toReportDetailList(
      reportDetailScoreWebRequest, REPORT_ID);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getUser()
                 .getId()).isEqualTo(STUDENT_ID);
    assertThat(actual.get(0)
                 .getPoint()).isEqualTo(SCORE);
    verify(validator).validate(reportDetailScoreWebRequest);
  }

  @Test
  public void toReportDetailListNullRequest() {

    catchException(() -> requestMapper.toReportDetailList(null, REPORT_ID));
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
  }

}
