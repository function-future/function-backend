package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.core.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReportDetailServiceImplTest {

    private static final String REPORT_ID = "report-id";
    private static final String REPORT_DETAIL_ID = "report-detail-id";
    private static final String USER_ID = "user-id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    private Report report;
    private ReportDetail reportDetail;
    private User student;

    @InjectMocks
    private ReportDetailServiceImpl reportDetailService;

    @Mock
    private ReportDetailRepository reportDetailRepository;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        report = Report.builder()
                .id(REPORT_ID)
                .title(TITLE)
                .description(DESCRIPTION)
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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAllByReportId() {
    }

    @Test
    public void createReportDetailByReport() {
    }

    @Test
    public void findByStudentId() {
    }

    @Test
    public void giveScoreToEachStudentInDetail() {
    }

    @Test
    public void deleteAllByReportId() {
    }
}