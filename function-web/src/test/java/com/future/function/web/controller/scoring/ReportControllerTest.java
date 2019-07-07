package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.service.api.feature.scoring.ReportService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.ReportRequestMapper;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest extends TestHelper {

    private static final String STUDENT_ID = "student-id";
    private static final String REPORT_ID = "report-id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String BATCH_CODE = "batch-code";

    private LocalDate usedAt;
    private ReportWebResponse reportWebResponse;
    private ReportWebRequest reportWebRequest;
    private Report report;
    private Pageable pageable;
    private Paging paging;
    private Batch batch;

    private DataResponse<ReportWebResponse> DATA_RESPONSE;
    private DataResponse<ReportWebResponse> CREATED_DATA_RESPONSE;
    private PagingResponse<ReportWebResponse> PAGING_RESPONSE;
    private JacksonTester<ReportWebRequest> webRequestJacksonTester;

    @MockBean
    private ReportService reportService;

    @MockBean
    private ReportRequestMapper requestMapper;

    @Before
    public void setUp() {
        super.setUp();
        super.setCookie(Role.ADMIN);

        paging = new Paging(1, 10, 1);

        pageable = new PageRequest(0, 10);

        usedAt = LocalDate.now().atStartOfDay().toLocalDate();

        batch = Batch.builder().code(BATCH_CODE).build();

        report = Report.builder()
                .id(REPORT_ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .batch(batch)
                .usedAt(usedAt)
                .studentIds(Collections.singletonList(STUDENT_ID))
                .build();

        reportWebRequest = ReportWebRequest.builder()
                .name(TITLE)
                .description(DESCRIPTION)
                .usedAt(usedAt.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond())
                .batchCode(BATCH_CODE)
                .students(Collections.singletonList(STUDENT_ID))
                .build();

        reportWebResponse = ReportWebResponse.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .usedAt(usedAt.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond())
                .batchCode(BATCH_CODE)
                .studentCount(1)
                .build();

        DATA_RESPONSE = ResponseHelper.toDataResponse(HttpStatus.OK, reportWebResponse);
        CREATED_DATA_RESPONSE = ResponseHelper.toDataResponse(HttpStatus.CREATED, reportWebResponse);
        PAGING_RESPONSE = ResponseHelper.toPagingResponse(HttpStatus.OK,
                Collections.singletonList(reportWebResponse), paging);
        when(reportService.findAllReport(BATCH_CODE, ADMIN_ID, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(report), pageable, 1));
        when(reportService.createReport(report)).thenReturn(report);
        when(reportService.updateReport(report)).thenReturn(report);
        when(reportService.findById(REPORT_ID)).thenReturn(report);
        when(requestMapper.toReport(reportWebRequest)).thenReturn(report);
        when(requestMapper.toReport(reportWebRequest, REPORT_ID)).thenReturn(report);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(reportService, requestMapper);
    }

    @Test
    public void findAllReportByUsedAtNow() throws Exception {
        mockMvc.perform(
                get("/api/scoring/batches/" + BATCH_CODE + "/final-judgings")
                        .cookie(cookies)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        pagingResponseJacksonTester.write(PAGING_RESPONSE)
                                .getJson()));
        verify(reportService).findAllReport(BATCH_CODE, ADMIN_ID, pageable);
    }

    @Test
    public void findOne() throws Exception {
        mockMvc.perform(
                get("/api/scoring/batches/" + BATCH_CODE + "/final-judgings/" + REPORT_ID)
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(DATA_RESPONSE)
                                .getJson()));
        verify(reportService).findById(REPORT_ID);
    }

    @Test
    public void createFinalJudging() throws Exception {
        mockMvc.perform(
                post("/api/scoring/batches/" + BATCH_CODE + "/final-judgings")
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(webRequestJacksonTester.write(reportWebRequest).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                                .getJson()));
        verify(requestMapper).toReport(reportWebRequest);
        verify(reportService).createReport(report);
    }

    @Test
    public void updateFinalJudging() throws Exception {
        mockMvc.perform(
                put("/api/scoring/batches/" + BATCH_CODE + "/final-judgings/" + REPORT_ID)
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(webRequestJacksonTester.write(reportWebRequest).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(DATA_RESPONSE)
                                .getJson()));
        verify(requestMapper).toReport(reportWebRequest, REPORT_ID);
        verify(reportService).updateReport(report);
    }

    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(
                delete("/api/scoring/batches/" + BATCH_CODE + "/final-judgings/" + REPORT_ID)
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        baseResponseJacksonTester.write(ResponseHelper.toBaseResponse(HttpStatus.OK))
                                .getJson()));
        verify(reportService).deleteById(REPORT_ID);
    }
}