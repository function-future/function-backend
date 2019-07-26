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
import java.util.Collection;
import java.util.List;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    private Report report;
    private Pageable pageable;
    private Page<Report> reportPage;
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

        batch = Batch.builder().code(BATCH_CODE).build();

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
                .report(report)
                .user(student)
                .point(0)
                .build();

        studentSummaryVO = StudentSummaryVO
            .builder()
            .studentName(STUDENT_NAME)
            .point(POINT)
            .build();

        pageable = new PageRequest(0, 10);

        reportPage = new PageImpl<>(Collections.singletonList(report), pageable, 1);

        when(reportRepository.findAll(pageable)).thenReturn(reportPage);
        when(reportRepository.findAllByBatchAndDeletedFalse(batch, pageable))
                .thenReturn(reportPage);
        when(reportRepository.findByIdAndDeletedFalse(REPORT_ID)).thenReturn(Optional.of(report));
        when(reportRepository.save(report)).thenReturn(report);
        when(userService.getUser(USER_ID)).thenReturn(student);
        when(reportDetailService.findAllDetailByReportId(REPORT_ID)).thenReturn(Collections.singletonList(reportDetail));
        when(reportDetailService.createReportDetailByReport(report, student)).thenReturn(report);
        when(reportDetailService.findAllSummaryByReportId(REPORT_ID, USER_ID)).thenReturn(Collections.singletonList(studentSummaryVO));
        when(reportDetailService.giveScoreToEachStudentInDetail(report, Collections.singletonList(reportDetail)))
            .thenReturn(Collections.singletonList(reportDetail));
        when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(reportDetailService, reportRepository, batchService);
    }

    @Test
    public void findAllReport() {
        User admin = User.builder().id(USER_ID).role(Role.ADMIN).build();
        when(userService.getUser(USER_ID)).thenReturn(admin);
        Page<Report> actual = reportService.findAllReport(BATCH_CODE, pageable);
        assertThat(actual).isEqualTo(reportPage);
        verify(reportRepository).findAllByBatchAndDeletedFalse(batch, pageable);
        verify(batchService).getBatchByCode(BATCH_CODE);
        verify(reportDetailService).findAllDetailByReportId(REPORT_ID);
    }

    @Test
    public void findAllReportJudge() {
        User judge = User.builder().id(USER_ID).role(Role.JUDGE).build();
        when(userService.getUser(USER_ID)).thenReturn(judge);
        Page<Report> actual = reportService.findAllReport(BATCH_CODE, pageable);
        assertThat(actual).isEqualTo(reportPage);
        verify(reportRepository).findAllByBatchAndDeletedFalse(batch, pageable);
        verify(batchService).getBatchByCode(BATCH_CODE);
        verify(reportDetailService).findAllDetailByReportId(REPORT_ID);
    }

    @Test
    public void findById() {
        Report actual = reportService.findById(REPORT_ID);
        assertThat(actual).isEqualTo(report);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportDetailService).findAllDetailByReportId(REPORT_ID);
    }

    @Test
    public void findByIdNullId() {
        catchException(() -> reportService.findById(null));
        assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    public void createReport() {
        report.setStudentIds(Collections.singletonList(USER_ID));
        Report actual = reportService.createReport(report);
        report.setStudentIds(null);
        assertThat(actual).isEqualTo(report);
        verify(reportRepository).save(report);
        verify(reportDetailService).createReportDetailByReport(report, student);
        verify(batchService).getBatchByCode(BATCH_CODE);
    }

    @Test
    public void createReportThrowException() {
        report.setStudentIds(null);
        catchException(() -> reportService.createReport(report));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
    }

    @Test
    public void createReportMoreThanThreeStudentIdsThrowException() {
        report.setStudentIds(Arrays.asList("id-1", "id-2", "id-3", "id-4"));
        catchException(() -> reportService.createReport(report));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
    }

    @Test
    public void updateReport() {
        report.setStudentIds(Collections.singletonList(USER_ID));
        reportDetail.setUser(student);
        Report actual = reportService.updateReport(report);
        assertThat(actual).isEqualTo(actual);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportRepository).save(report);
        verify(reportDetailService, times(2)).findAllDetailByReportId(REPORT_ID);
    }

    @Test
    public void updateReportDifferentStudent() {
        String id = "another-student";
        User anotherStudent = User.builder().id(id).role(Role.STUDENT).build();
        when(userService.getUser(id)).thenReturn(anotherStudent);
        when(reportDetailService.createReportDetailByReport(report, anotherStudent)).thenReturn(report);
        report.setStudentIds(Collections.singletonList(id));
        reportDetail.setUser(student);
        Report actual = reportService.updateReport(report);
        assertThat(actual).isEqualTo(actual);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportRepository).save(report);
        verify(reportDetailService, times(2)).findAllDetailByReportId(REPORT_ID);
        verify(reportDetailService).createReportDetailByReport(report, anotherStudent);
        verify(reportDetailService).deleteAllByReportId(REPORT_ID);
    }

    @Test
    public void updateReportEmptyStudentIds() {
        report.setStudentIds(Collections.emptyList());
        reportDetail.setUser(student);
        catchException(() -> reportService.updateReport(report));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
        verify(reportDetailService).findAllDetailByReportId(REPORT_ID);
    }

    @Test
    public void updateReportMoreThanThreeStudentIds() {
        report.setStudentIds(Arrays.asList("id-1", "id-2", "id-3", "id-4"));
        reportDetail.setUser(student);
        catchException(() -> reportService.updateReport(report));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
        verify(reportDetailService).findAllDetailByReportId(REPORT_ID);
    }

    @Test
    public void deleteById() {
        reportService.deleteById(REPORT_ID);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportDetailService).deleteAllByReportId(REPORT_ID);
        Report deletedReport = new Report();
        BeanUtils.copyProperties(report, deletedReport);
        deletedReport.setDeleted(true);
        verify(reportRepository).save(deletedReport);
    }

    @Test
    public void findAllSummaryByReportId() {
        List<StudentSummaryVO> actual = reportService.findAllSummaryByReportId(REPORT_ID, USER_ID);
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getPoint()).isEqualTo(POINT);
        assertThat(actual.get(0).getStudentName()).isEqualTo(STUDENT_NAME);
        verify(reportDetailService).findAllSummaryByReportId(REPORT_ID, USER_ID);
    }

    @Test
    public void giveScoreToReportStudentsTest() {
        reportDetail.setPoint(POINT);
        List<ReportDetail> actual = reportService.giveScoreToReportStudents(REPORT_ID, Collections.singletonList(reportDetail));
        assertThat(actual.get(0).getPoint()).isEqualTo(POINT);
        assertThat(actual.size()).isEqualTo(1);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportDetailService).giveScoreToEachStudentInDetail(report, Collections.singletonList(reportDetail));
    }

    @Test
    public void giveScoreToReportStudentsTestReportNotFound() {
        when(reportRepository.findByIdAndDeletedFalse(REPORT_ID)).thenReturn(Optional.empty());
        catchException(() -> reportService.giveScoreToReportStudents(REPORT_ID, Collections.singletonList(reportDetail)));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
    }
}
