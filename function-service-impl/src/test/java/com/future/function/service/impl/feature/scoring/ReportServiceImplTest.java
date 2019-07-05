package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.repository.feature.scoring.ReportRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
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
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final LocalDate USED_AT = LocalDate.now(ZoneId.systemDefault());

    private Report report;
    private Pageable pageable;
    private Page<Report> reportPage;
    private ReportDetail reportDetail;
    private User student;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportDetailService reportDetailService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserService userService;


    @Before
    public void setUp() throws Exception {
        report = Report.builder()
                .id(REPORT_ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .usedAt(USED_AT)
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

        pageable = new PageRequest(0, 10);

        reportPage = new PageImpl<>(Collections.singletonList(report), pageable, 1);

        when(reportRepository.findAll(pageable)).thenReturn(reportPage);
        when(reportRepository.findAllByUsedAtEqualsAndDeletedFalse(LocalDate.now(), pageable)).thenReturn(reportPage);
        when(reportRepository.findByIdAndDeletedFalse(REPORT_ID)).thenReturn(Optional.of(report));
        when(reportRepository.save(report)).thenReturn(report);
        when(userService.getUser(USER_ID)).thenReturn(student);
        when(reportDetailService.findAllDetailByReportId(REPORT_ID)).thenReturn(Collections.singletonList(reportDetail));
        when(reportDetailService.createReportDetailByReport(report, student)).thenReturn(report);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(reportDetailService, reportRepository);
    }

    @Test
    public void findAllReport() {
        User admin = User.builder().id(USER_ID).role(Role.ADMIN).build();
        when(userService.getUser(USER_ID)).thenReturn(admin);
        Page<Report> actual = reportService.findAllReport(USER_ID, pageable);
        assertThat(actual).isEqualTo(reportPage);
        verify(reportRepository).findAll(pageable);
        verify(userService).getUser(USER_ID);
    }

    @Test
    public void findAllReportJudge() {
        User judge = User.builder().id(USER_ID).role(Role.JUDGE).build();
        when(userService.getUser(USER_ID)).thenReturn(judge);
        Page<Report> actual = reportService.findAllReport(USER_ID, pageable);
        assertThat(actual).isEqualTo(reportPage);
        verify(reportRepository).findAllByUsedAtEqualsAndDeletedFalse(USED_AT, pageable);
        verify(userService).getUser(USER_ID);
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
        report.setStudentIds(Collections.singletonList(USER_ID));
        Report actual = reportService.createReport(report);
        report.setStudentIds(null);
        assertThat(actual).isEqualTo(report);
        verify(reportRepository).save(report);
        verify(reportDetailService).createReportDetailByReport(report, student);
    }

    @Test
    public void createReportThrowException() {
        report.setStudentIds(null);
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
        verify(reportDetailService).findAllDetailByReportId(REPORT_ID);
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
    public void giveScoreToEachStudent() {
        reportDetail.setPoint(100);
        when(reportDetailService.giveScoreToEachStudentInDetail(REPORT_ID, Collections.singletonList(reportDetail),
                USER_ID)).thenReturn(report);
        Report actual = reportService.giveScoreToEachStudent(REPORT_ID, Collections.singletonList(reportDetail), USER_ID);
        assertThat(actual).isEqualTo(report);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportDetailService).giveScoreToEachStudentInDetail(REPORT_ID,
                Collections.singletonList(reportDetail), USER_ID);
    }

    @Test
    public void giveScoreToEachStudentThrowException() {
        reportDetail.setPoint(100);
        catchException(() -> reportService.giveScoreToEachStudent(REPORT_ID, Collections.singletonList(reportDetail),
                USER_ID));
        assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
        verify(reportRepository).findByIdAndDeletedFalse(REPORT_ID);
        verify(reportDetailService).giveScoreToEachStudentInDetail(REPORT_ID, Collections.singletonList(reportDetail),
                USER_ID);
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
}
