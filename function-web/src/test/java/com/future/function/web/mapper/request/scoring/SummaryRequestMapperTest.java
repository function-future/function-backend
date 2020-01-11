package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ScoreStudentWebRequest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SummaryRequestMapperTest {

  private static final String STUDENT_ID = "student-id";

  private static final int SCORE = 100;

  private ScoreStudentWebRequest scoreStudentWebRequest;

  @InjectMocks
  private SummaryRequestMapper requestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() throws Exception {

    scoreStudentWebRequest = ScoreStudentWebRequest.builder()
      .studentId(STUDENT_ID)
      .score(SCORE)
      .build();
    when(validator.validate(scoreStudentWebRequest)).thenReturn(
        scoreStudentWebRequest);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(validator);
  }

  @Test
  public void toReportDetail() {

    ReportDetail actual = requestMapper.toReportDetail(
      scoreStudentWebRequest);
    assertThat(actual.getUser().getId()).isEqualTo(STUDENT_ID);
    assertThat(actual.getPoint()).isEqualTo(SCORE);
    verify(validator).validate(scoreStudentWebRequest);
  }

  @Test
  public void toReportDetailNullRequest() {

    catchException(() -> requestMapper.toReportDetail(null));
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
  }

}
