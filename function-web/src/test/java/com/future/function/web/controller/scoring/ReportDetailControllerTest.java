package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.dto.scoring.StudentSummaryDTO;
import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.ReportDetailRequestMapper;
import com.future.function.web.model.request.scoring.ReportDetailScoreWebRequest;
import com.future.function.web.model.request.scoring.ScoreStudentWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import com.future.function.web.model.response.feature.scoring.SummaryWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(ReportDetailController.class)
public class ReportDetailControllerTest extends TestHelper {

    private static final String STUDENT_ID = "student-id";
    private static final String REPORT_ID = "report-id";
    private static final String STUDENT_NAME = "student-name";
    private static final String UNIVERSITY = "university";
    private static final String AVATAR = "avatar";
    private static final String BATCH_CODE = "batch-code";
    private static final String TITLE = "quiz-title";
    private static final String TYPE = "type";
    private static final int POINT = 100;

    private ReportDetail reportDetail;
    private StudentSummaryDTO studentSummaryDTO;
    private SummaryDTO summaryDTO;
    private User student;
    private Batch batch;
    private FileV2 fileV2;
    private Report report;

    private ReportDetailWebResponse reportDetailWebResponse;
    private ReportDetailWebResponse createdReportDetailWebResponse;
    private SummaryWebResponse summaryWebResponse;
    private ScoreStudentWebRequest scoreStudentWebRequest;
    private ReportDetailScoreWebRequest reportDetailScoreWebRequest;

    private DataResponse<List<ReportDetailWebResponse>> DATA_RESPONSE;
    private DataResponse<List<ReportDetailWebResponse>> CREATED_DATA_RESPONSE;

    private JacksonTester<ReportDetailScoreWebRequest> webRequestJacksonTester;

    @MockBean
    private ReportDetailService reportDetailService;

    @MockBean
    private ReportDetailRequestMapper requestMapper;

    @Before
    public void setUp() {
        super.setUp();
        super.setCookie(Role.JUDGE);

        report = Report.builder().id(REPORT_ID).build();
        fileV2 = FileV2.builder().fileUrl(AVATAR).build();
        batch = Batch.builder().code(BATCH_CODE).build();
        summaryDTO = SummaryDTO.builder().title(TITLE).type(TYPE).point(POINT).build();
        student = User.builder().pictureV2(fileV2).batch(batch).name(STUDENT_NAME).university(UNIVERSITY).build();
        reportDetail = ReportDetail.builder().user(student).point(POINT).report(report).build();
        studentSummaryDTO = StudentSummaryDTO.builder()
                .studentName(STUDENT_NAME)
                .university(UNIVERSITY)
                .batchCode(BATCH_CODE)
                .avatar(AVATAR)
                .scores(Collections.singletonList(summaryDTO))
                .build();
        scoreStudentWebRequest = ScoreStudentWebRequest.builder()
                .studentId(STUDENT_ID)
                .score(POINT)
                .build();
        reportDetailScoreWebRequest = ReportDetailScoreWebRequest.builder()
                .scores(Collections.singletonList(scoreStudentWebRequest))
                .build();
        summaryWebResponse = SummaryWebResponse.builder()
                .title(TITLE)
                .type(TYPE)
                .point(POINT)
                .build();
        reportDetailWebResponse = ReportDetailWebResponse.builder()
                .studentName(STUDENT_NAME)
                .university(UNIVERSITY)
                .batchCode(BATCH_CODE)
                .avatar(AVATAR)
                .scores(Collections.singletonList(summaryWebResponse))
                .build();
        createdReportDetailWebResponse = ReportDetailWebResponse.builder()
                .studentName(STUDENT_NAME)
                .university(UNIVERSITY)
                .batchCode(BATCH_CODE)
                .avatar(AVATAR)
                .point(POINT)
                .build();

        DATA_RESPONSE = ResponseHelper.toDataResponse(HttpStatus.OK, Collections.singletonList(reportDetailWebResponse));
        CREATED_DATA_RESPONSE = ResponseHelper.toDataResponse(HttpStatus.CREATED,
                Collections.singletonList(createdReportDetailWebResponse));

        when(requestMapper.toReportDetailList(reportDetailScoreWebRequest, REPORT_ID))
                .thenReturn(Collections.singletonList(reportDetail));
        when(reportDetailService.findAllSummaryByReportId(REPORT_ID, JUDGE_ID))
                .thenReturn(Collections.singletonList(studentSummaryDTO));
        when(reportDetailService.giveScoreToEachStudentInDetail(REPORT_ID, Collections.singletonList(reportDetail)))
                .thenReturn(Collections.singletonList(reportDetail));
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(reportDetailService, requestMapper);
    }

    @Test
    public void findComparisonByReportId() throws Exception {
        mockMvc.perform(
                get("/api/scoring/batches/" + BATCH_CODE + "/final-judgings/" + REPORT_ID + "/comparison")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(DATA_RESPONSE)
                                .getJson()));
        verify(reportDetailService).findAllSummaryByReportId(REPORT_ID, JUDGE_ID);
    }

    @Test
    public void giveFinalScoreToStudentsByReportId() throws Exception {
        mockMvc.perform(
                post("/api/scoring/batches/" + BATCH_CODE + "/final-judgings/" + REPORT_ID + "/comparison")
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(webRequestJacksonTester.write(reportDetailScoreWebRequest).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                                .getJson()));
        verify(requestMapper).toReportDetailList(reportDetailScoreWebRequest, REPORT_ID);
        verify(reportDetailService).giveScoreToEachStudentInDetail(REPORT_ID, Collections.singletonList(reportDetail));
    }
}