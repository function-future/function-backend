package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import java.util.Collections;
import java.util.List;
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
public class ReportRequestMapperTest {

  private static final String NAME = "final-judge";
  private static final String DESCRIPTION = "final description";
  private static final long USED_AT = 1561607367;
  private static final String STUDENT_ID = "student-id";

  private ReportWebRequest request;
  private List<String> studentIds;

  @InjectMocks
  private ReportRequestMapper requestMapper;

  @Mock
  private RequestValidator requestValidator;

  @Before
  public void setUp() throws Exception {

    studentIds = Collections.singletonList(STUDENT_ID);

    request = ReportWebRequest
        .builder()
        .name(NAME)
        .description(DESCRIPTION)
        .usedAt(USED_AT)
        .students(studentIds)
        .build();

    when(requestValidator.validate(request)).thenReturn(request);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(requestValidator);
  }

  @Test
  public void toReport() {
    Report actual = requestMapper.toReport(request);
    assertThat(actual.getTitle()).isEqualTo(NAME);
    assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(actual.getStudentIds()).isEqualTo(studentIds);
    verify(requestValidator).validate(request);
  }

  @Test
  public void toReportWithNullValue() {
    request = null;
    catchException(() -> requestMapper.toReport(request));
    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
  }

  @Test
  public void toReportWithId() {
    Report actual = requestMapper.toReport(request, "requestId");
    assertThat(actual.getId()).isEqualTo("requestId");
    verify(requestValidator).validate(request);
  }
}
