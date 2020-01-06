package com.future.function.web.controller.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.request.scoring.SummaryRequestMapper;
import com.future.function.web.mapper.response.scoring.ReportDetailResponseMapper;
import com.future.function.web.model.request.scoring.ScoreStudentWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import com.future.function.web.model.response.feature.scoring.SummaryWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

  private Pageable pageable;

  private User user;

  private ReportDetail reportDetail;

  private StudentSummaryVO studentSummaryVO;

  private SummaryWebResponse summaryWebResponse;

  private ScoreStudentWebRequest scoreStudentWebRequest;

  private DataResponse<ReportDetailWebResponse> DATA_RESPONSE;

  private JacksonTester<ScoreStudentWebRequest> scoreStudentWebRequestJacksonTester;

  @MockBean
  private ReportDetailService reportDetailService;

  @MockBean
  private SummaryRequestMapper requestMapper;

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
      .scores(new PageImpl<>(Collections.singletonList(summaryVO)))
      .build();

    summaryWebResponse = SummaryWebResponse.builder()
      .title(TITLE)
      .type(TYPE)
      .point(POINT)
      .build();

    user = User.builder()
        .id(STUDENT_ID)
        .name(STUDENT_NAME)
        .role(Role.STUDENT)
        .batch(Batch.builder().code(BATCH_CODE).build())
        .build();

    reportDetail = ReportDetail.builder()
        .user(user)
        .point(100)
        .build();

    scoreStudentWebRequest = ScoreStudentWebRequest.builder()
        .studentId(STUDENT_ID)
        .score(100)
        .build();

    pageable = new PageRequest(0, 10);

    DATA_RESPONSE = ReportDetailResponseMapper.toDataReportDetailWebResponse(
      studentSummaryVO, URL_PREFIX);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(reportDetailService.findSummaryByStudentId(STUDENT_ID, ADMIN_ID, "quiz", pageable))
        .thenReturn(studentSummaryVO);
    when(reportDetailService.giveScoreToEachStudentInDetail(reportDetail))
        .thenReturn(reportDetail);
    when(requestMapper.toReportDetail(scoreStudentWebRequest)).thenReturn(reportDetail);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(reportDetailService, fileProperties, requestMapper);
  }

  @Test
  public void findAllSummaryByStudentId() throws Exception {

    mockMvc.perform(get("/api/scoring/summary/" + STUDENT_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));
    verify(reportDetailService).findSummaryByStudentId(STUDENT_ID, ADMIN_ID, "quiz", pageable);
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void giveStudentFinalScoreByRequest() throws Exception {
    super.setCookie(Role.JUDGE);
    DATA_RESPONSE = ReportDetailResponseMapper.toDataReportDetailWebResponse(
        HttpStatus.CREATED, reportDetail, URL_PREFIX);
    mockMvc.perform(post("/api/scoring/summary")
        .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(scoreStudentWebRequestJacksonTester.write(scoreStudentWebRequest).getJson())
    )
        .andExpect(status().isCreated())
        .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));
    verify(requestMapper).toReportDetail(scoreStudentWebRequest);
    verify(reportDetailService).giveScoreToEachStudentInDetail(reportDetail);
    verify(fileProperties).getUrlPrefix();
  }
}
