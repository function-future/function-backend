package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.response.scoring.ReportDetailResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import com.future.function.web.model.response.feature.scoring.SummaryWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import sun.security.x509.AVA;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(SummaryController.class)
public class SummaryControllerTest extends TestHelper {

  private static final String TITLE = "title";
  private static final String TYPE = "type";
  private static final String STUDENT_ID = "student-another-id";
  private static final String STUDENT_NAME = "student-name";
  private static final String BATCH_CODE = "batch-code";
  private static final String UNIVERSITY = "university";
  private static final int POINT = 100;
  private static final String URL_PREFIX = "url-prefix";
  private static final String AVATAR = "avatar";

    private SummaryVO summaryVO;
    private StudentSummaryVO studentSummaryVO;
  private SummaryWebResponse summaryWebResponse;
  private DataResponse<ReportDetailWebResponse> DATA_RESPONSE;

  @MockBean
  private SummaryService summaryService;

  @MockBean
  private FileProperties fileProperties;

  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.ADMIN);

      summaryVO = SummaryVO.builder()
        .title(TITLE)
        .type(TYPE)
        .point(POINT)
        .build();

      studentSummaryVO = StudentSummaryVO.builder()
        .studentName(STUDENT_NAME)
        .batchCode(BATCH_CODE)
        .university(UNIVERSITY)
        .avatar(AVATAR)
        .scores(Collections.singletonList(summaryVO))
        .build();

    summaryWebResponse = SummaryWebResponse.builder()
        .title(TITLE)
        .type(TYPE)
        .point(POINT)
        .build();

      DATA_RESPONSE = ReportDetailResponseMapper.toDataReportDetailWebResponse(studentSummaryVO, URL_PREFIX);

      when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
      when(summaryService.findAllPointSummaryByStudentId(STUDENT_ID, ADMIN_ID)).thenReturn(studentSummaryVO);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(summaryService);
  }

  @Test
  public void findAllSummaryByStudentId() throws Exception {
    mockMvc.perform(
        get("/api/scoring/summary/" + STUDENT_ID)
            .cookie(cookies))
        .andExpect(status().isOk())
        .andExpect(content().json(
            dataResponseJacksonTester.write(DATA_RESPONSE).getJson()));
    verify(summaryService).findAllPointSummaryByStudentId(STUDENT_ID, ADMIN_ID);
  }
}
