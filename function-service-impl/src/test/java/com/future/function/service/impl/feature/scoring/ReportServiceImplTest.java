package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.repository.feature.scoring.ReportRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceImplTest {

  private static final String REPORT_ID = "report-id";

  private static final String REPORT_DETAIL_ID = "report-detail-id";

  private static final String USER_ID = "user-id";

  private static final String STUDENT_NAME = "user-name";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final String BATCH_CODE = "batch-code";

  private static final Integer POINT = 100;

  private static final String TYPE = "ASSIGNMENT";

  private static final Integer FINAL_POINT = 100;

  private Report report;

  private Pageable pageable;

  private Pair pair;

  private Page<Report> reportPage;

  private Page<Pair<User, Integer>> pairPage;

  private ReportDetail reportDetail;

  private StudentSummaryVO studentSummaryVO;

  private User student;

  private Batch batch;

  @InjectMocks
  private ReportServiceImpl reportService;

  @Mock
  private ReportDetailService reportDetailService;

  @Mock
  private ReportRepository reportRepository;

  @Mock
  private UserService userService;

  @Mock
  private BatchService batchService;


  @Before
  public void setUp() throws Exception {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();

    report = Report.builder()
      .id(REPORT_ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .batch(batch)
      .build();

    student = User.builder()
      .id(USER_ID)
      .role(Role.STUDENT)
      .build();

    reportDetail = ReportDetail.builder()
      .id(REPORT_DETAIL_ID)
      .user(student)
      .point(POINT)
      .build();

    studentSummaryVO = StudentSummaryVO.builder()
      .studentName(STUDENT_NAME)
      .point(POINT)
      .build();

    pageable = new PageRequest(0, 10);

    pair = Pair.of(student, 100);

    reportPage = new PageImpl<>(Collections.singletonList(report), pageable, 1);

    pairPage = new PageImpl<>(Collections.singletonList(pair), pageable, 1);

    when(reportRepository.findAll(pageable)).thenReturn(reportPage);
    when(reportRepository.findAllByBatchAndDeletedFalse(batch,
                                                        pageable
    )).thenReturn(reportPage);
    when(reportRepository.findByIdAndDeletedFalse(REPORT_ID)).thenReturn(
      Optional.of(report));
    when(reportRepository.save(report)).thenReturn(report);
    when(userService.getUser(USER_ID)).thenReturn(student);
    when(userService.getStudentsWithinBatch(BATCH_CODE, pageable)).thenReturn(
      new PageImpl<>(Collections.singletonList(student), pageable, 1));
    when(reportDetailService.findByStudentId(USER_ID, USER_ID)).thenReturn(
      reportDetail);
    when(reportDetailService.createOrGetReportDetail(student)).thenReturn(reportDetail);
    when(reportDetailService.findAllSummaryByReportId(report,
                                                      USER_ID, TYPE, pageable
    )).thenReturn(Collections.singletonList(studentSummaryVO));
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(
      reportDetailService, reportRepository, batchService);
  }

  @Test
  public void findAllReport() {

    User admin = User.builder()
      .id(USER_ID)
      .role(Role.ADMIN)
      .build();
    when(userService.getUser(USER_ID)).thenReturn(admin);
    Page<Report> actual = reportService.findAllReport(BATCH_CODE, pageable);
    assertThat(actual).isEqualTo(reportPage);
    verify(reportRepository).findAllByBatchAndDeletedFalse(batch, pageable);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void findAllReportJudge() {

    User judge = User.builder()
      .id(USER_ID)
      .role(Role.JUDGE)
      .build();
    when(userService.getUser(USER_ID)).thenReturn(judge);
    Page<Report> actual = reportService.findAllReport(BATCH_CODE, pageable);
    assertThat(actual).isEqualTo(reportPage);
    verify(reportRepository).findAllByBatchAndDeletedFalse(batch, pageable);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void findAllStudentsAndFinalPointByBatch() {

    Page<Pair<User, Integer>> actual =
      reportService.findAllStudentsAndFinalPointByBatch(BATCH_CODE, pageable);
    assertThat(actual.getContent()).isEqualTo(Collections.singletonList(pair));
    verify(userService).getStudentsWithinBatch(BATCH_CODE, pageable);
    verify(reportDetailService).findByStudentId(USER_ID, USER_ID);
  }

  @Test
  public void findAllStudentsAndFinalPointByBatchAndReportDetailNull() {

    when(reportDetailService.findByStudentId(USER_ID, USER_ID)).thenReturn(
      null);
    pair = Pair.of(student, 0);
    Page<Pair<User, Integer>> actual =
      reportService.findAllStudentsAndFinalPointByBatch(BATCH_CODE, pageable);
    assertThat(actual.getContent()).isEqualTo(Collections.singletonList(pair));
    verify(userService).getStudentsWithinBatch(BATCH_CODE, pageable);
    verify(reportDetailService).findByStudentId(USER_ID, USER_ID);
  }

  @Test
  public void findById() {

    Report actual = reportService.findById(REPORT_ID);
    assertThat(actual).isEqualTo(report);
    verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
  }

  @Test
  public void findByIdNullId() {

    catchException(() -> reportService.findById(null));
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
  }

  @Test
  public void createReport() {
    when(reportRepository.findByStudentsAndDeletedFalse(Collections.singletonList(reportDetail))).thenReturn(Optional.empty());
    report.setStudents(Collections.singletonList(reportDetail));
    Report actual = reportService.createReport(report);
    report.setStudents(null);
    assertThat(actual).isEqualTo(report);
    verify(reportRepository).save(report);
    verify(reportDetailService).createOrGetReportDetail(student);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(reportRepository).findByStudentsAndDeletedFalse(Collections.singletonList(reportDetail));
  }

  @Test
  public void createReportStudentsAlreadyExist() {
    when(reportRepository.findByStudentsAndDeletedFalse(Collections.singletonList(reportDetail))).thenReturn(Optional.of(report));
    report.setStudents(Collections.singletonList(reportDetail));
    catchException(() -> reportService.createReport(report));
    assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
    verify(reportDetailService).createOrGetReportDetail(student);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(reportRepository).findByStudentsAndDeletedFalse(Collections.singletonList(reportDetail));
  }

  @Test
  public void createReportThrowException() {

    report.setStudents(null);
    catchException(() -> reportService.createReport(report));
    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void updateReport() {

    report.setStudents(Collections.singletonList(reportDetail));
    reportDetail.setUser(student);
    Report actual = reportService.updateReport(report);
    assertThat(actual).isEqualTo(actual);
    verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
    verify(reportRepository).save(report);
  }

  @Test
  public void updateReportDifferentStudent() {

    String id = "another-student";
    User anotherStudent = User.builder()
      .id(id)
      .role(Role.STUDENT)
      .build();
    when(userService.getUser(id)).thenReturn(anotherStudent);
    when(reportDetailService.createOrGetReportDetail(anotherStudent)).thenReturn(reportDetail);
    report.setStudents(Collections.singletonList(reportDetail));
    reportDetail.setUser(student);
    Report actual = reportService.updateReport(report);
    assertThat(actual).isEqualTo(actual);
    verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
    verify(reportRepository).save(report);
  }

  @Test
  public void deleteById() {

    reportService.deleteById(REPORT_ID);
    verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
    Report deletedReport = new Report();
    BeanUtils.copyProperties(report, deletedReport);
    deletedReport.setDeleted(true);
    verify(reportRepository).save(deletedReport);
  }

  @Test
  public void findAllSummaryByReportId() {

    List<StudentSummaryVO> actual = reportService.findAllSummaryByReportId(
      REPORT_ID, USER_ID, TYPE, pageable);
    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.get(0)
                 .getPoint()).isEqualTo(POINT);
    assertThat(actual.get(0)
                 .getStudentName()).isEqualTo(STUDENT_NAME);
    verify(reportDetailService).findAllSummaryByReportId(report, USER_ID, TYPE, pageable);
    verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
  }

}
