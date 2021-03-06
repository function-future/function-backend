package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.SummaryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportDetailServiceImplTest {

  private static final String REPORT_ID = "report-id";

  private static final String REPORT_DETAIL_ID = "report-detail-id";

  private static final String USER_ID = "user-id";

  private static final String USER_NAME = "student-name";

  private static final String UNIVERSITY = "university";

  private static final String BATCH_CODE = "batch-code";

  private static final String FILE_URL = "file-url";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final String QUIZ_TITLE = "quiz";

  private static final String QUIZ_TYPE = "QUIZ";

  private static final int POINT = 100;

  private static final String TYPE = "Assignment";

  private Report report;

  private Pageable pageable;

  private ReportDetail reportDetail;

  private User student;

  private Batch batch;

  private FileV2 fileV2;

  private SummaryVO summaryVO;

  private StudentSummaryVO studentSummaryVO;

  @InjectMocks
  private ReportDetailServiceImpl reportDetailService;

  @Mock
  private ReportDetailRepository reportDetailRepository;

  @Mock
  private UserService userService;

  @Mock
  private SummaryService summaryService;

  @Before
  public void setUp() throws Exception {

    summaryVO = SummaryVO.builder()
      .title(QUIZ_TITLE)
      .type(QUIZ_TYPE)
      .point(POINT)
      .build();

    studentSummaryVO = StudentSummaryVO.builder()
      .studentName(USER_NAME)
      .university(UNIVERSITY)
      .batchCode(BATCH_CODE)
      .avatar(FILE_URL)
      .scores(new PageImpl<>(Collections.singletonList(summaryVO)))
      .build();

    pageable = new PageRequest(0, 10);

    batch = Batch.builder()
        .code(BATCH_CODE)
        .build();

    fileV2 = FileV2.builder()
        .fileUrl(FILE_URL)
        .build();

    student = User.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .university(UNIVERSITY)
        .batch(batch)
        .pictureV2(fileV2)
        .role(Role.STUDENT)
        .build();

    reportDetail = ReportDetail.builder()
        .id(REPORT_DETAIL_ID)
        .user(student)
        .point(0)
        .build();

    report = Report.builder()
      .id(REPORT_ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .students(Collections.singletonList(reportDetail))
      .build();

    when(reportDetailRepository.findAll()).thenReturn(Collections.singletonList(reportDetail));
    when(reportDetailRepository.save(reportDetail)).thenReturn(reportDetail);
    when(
      reportDetailRepository.findByUserIdAndDeletedFalse(USER_ID)).thenReturn(
      Optional.of(reportDetail));
    when(summaryService.findAllPointSummaryByStudentId(USER_ID, pageable,
                                                       USER_ID, TYPE
    )).thenReturn(studentSummaryVO);
    when(userService.getUser(USER_ID)).thenReturn(student);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(reportDetailRepository, userService);
  }

  @Test
  public void findAllSummaryByReportId() {

    List<StudentSummaryVO> actual =
      reportDetailService.findAllSummaryByReportId(report, USER_ID, TYPE, pageable);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getStudentName()).isEqualTo(USER_NAME);
    assertThat(actual.get(0)
                 .getUniversity()).isEqualTo(UNIVERSITY);
    assertThat(actual.get(0)
                 .getBatchCode()).isEqualTo(BATCH_CODE);
    assertThat(actual.get(0)
                 .getAvatar()).isEqualTo(FILE_URL);
    assertThat(actual.get(0)
                 .getScores()
                 .getContent()
                 .get(0)
                 .getPoint()).isEqualTo(POINT);
    verify(summaryService).findAllPointSummaryByStudentId(USER_ID, pageable, USER_ID, TYPE);
  }

  @Test
  public void findAllSummaryByReportIdFailedToGetSummary() {

    when(summaryService.findAllPointSummaryByStudentId(USER_ID, pageable,
                                                       USER_ID, TYPE
    )).thenReturn(null);
    List<StudentSummaryVO> actual =
      reportDetailService.findAllSummaryByReportId(report, USER_ID, TYPE, pageable);
    assertThat(actual.size()).isEqualTo(0);
    verify(summaryService).findAllPointSummaryByStudentId(USER_ID, pageable, USER_ID, TYPE);
  }

  @Test
  public void createReportDetailByReport() {

    when(
      reportDetailRepository.findByUserIdAndDeletedFalse(USER_ID)).thenReturn(
      Optional.empty());
    when(reportDetailRepository.save(any(ReportDetail.class))).thenReturn(
      reportDetail);
    ReportDetail actual = reportDetailService.createOrGetReportDetail(student);
    assertThat(actual).isEqualTo(reportDetail);
    verify(reportDetailRepository).save(any(ReportDetail.class));
    verify(reportDetailRepository).findByUserIdAndDeletedFalse(USER_ID);
  }

  @Test
  public void findByStudentId() {

    ReportDetail actual = reportDetailService.findByStudentId(USER_ID, USER_ID);
    assertThat(actual).isEqualTo(reportDetail);
    verify(reportDetailRepository).findByUserIdAndDeletedFalse(USER_ID);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void findSummaryByStudentId() {

    StudentSummaryVO actual = reportDetailService.findSummaryByStudentId(USER_ID, USER_ID, TYPE, pageable);
    assertThat(actual).isEqualTo(studentSummaryVO);
    verify(reportDetailRepository).findByUserIdAndDeletedFalse(USER_ID);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void findByStudentIdAndNotExistsInDB() {

    when(
      reportDetailRepository.findByUserIdAndDeletedFalse(USER_ID)).thenReturn(
      Optional.empty());
    ReportDetail actual = reportDetailService.findByStudentId(USER_ID, USER_ID);
    assertThat(actual).isNull();
    verify(reportDetailRepository).findByUserIdAndDeletedFalse(USER_ID);
    verify(userService).getUser(USER_ID);
  }

  @Test
  public void findByStudentIdAccessedByAdmin() {

    String id = "id";
    when(userService.getUser(id)).thenReturn(User.builder()
                                               .id(id)
                                               .role(Role.ADMIN)
                                               .build());
    ReportDetail actual = reportDetailService.findByStudentId(USER_ID, id);
    assertThat(actual).isEqualTo(reportDetail);
    verify(reportDetailRepository).findByUserIdAndDeletedFalse(USER_ID);
    verify(userService).getUser(id);
  }

  @Test
  public void findByStudentIdAndUserIdNotEqual() {

    String id = "id";
    when(userService.getUser(id)).thenReturn(User.builder()
                                               .id(id)
                                               .role(Role.STUDENT)
                                               .build());
    catchException(() -> reportDetailService.findByStudentId(USER_ID, id));
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    verify(userService).getUser(id);
  }

  @Test
  public void giveScoreToEachStudentInDetail() {

    ReportDetail actual =
      reportDetailService.giveScoreToEachStudentInDetail(reportDetail);
    assertThat(actual).isEqualTo(reportDetail);
    verify(reportDetailRepository).findByUserIdAndDeletedFalse(USER_ID);
    verify(reportDetailRepository).save(reportDetail);
  }

  @Test
  public void deleteAllByReportId() {

    reportDetail.setDeleted(true);
    reportDetailService.deleteAll();
    verify(reportDetailRepository).findAll();
    verify(reportDetailRepository).save(reportDetail);
  }

}
