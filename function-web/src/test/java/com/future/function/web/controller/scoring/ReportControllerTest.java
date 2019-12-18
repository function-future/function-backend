package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.service.api.feature.scoring.ReportService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.ReportRequestMapper;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest extends TestHelper {

  private static final String STUDENT_ID = "student-id";

  private static final String STUDENT_NAME = "student-name";

  private static final String STUDENT_PHONE = "student-phone";

  private static final String STUDENT_AVATAR = "student-avatar";

  private static final String STUDENT_ADDRESS = "student-address";

  private static final String STUDENT_EMAIL = "student-email";

  private static final String STUDENT_UNIVERSITY = "student-university";

  private static final String REPORT_ID = "report-id";

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final String BATCH_CODE = "batch-code";

  private static final Long CREATED_AT = new Date().getTime();

  private static final String FILE_ID = "file-id";

  private static final String URL_PREFIX = "url-prefix/";

  private static final Integer FINAL_POINT = 100;

  private ReportWebResponse reportWebResponse;

  private UserWebResponse userWebResponse;

  private UserWebResponse userWebResponseWithFinalPoint;

  private ReportWebRequest reportWebRequest;

  private Report report;

  private User user;

  private ReportDetail reportDetail;

  private Pageable pageable;

  private Paging paging;

  private Batch batch;

  private DataResponse<ReportWebResponse> DATA_RESPONSE;

  private DataResponse<ReportWebResponse> CREATED_DATA_RESPONSE;

  private PagingResponse<ReportWebResponse> PAGING_RESPONSE;

  private PagingResponse<UserWebResponse> USER_PAGING_RESPONSE;

  private JacksonTester<ReportWebRequest> webRequestJacksonTester;

  @MockBean
  private ReportService reportService;

  @MockBean
  private ReportRequestMapper requestMapper;

  @MockBean
  private FileProperties fileProperties;

  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.ADMIN);

    paging = new Paging(1, 10, 1);

    pageable = new PageRequest(0, 10);

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();

    user = User.builder()
      .id(STUDENT_ID)
      .name(STUDENT_NAME)
      .role(Role.STUDENT)
      .address(STUDENT_ADDRESS)
      .phone(STUDENT_PHONE)
      .pictureV2(FileV2.builder()
                   .id(FILE_ID)
                   .fileUrl(STUDENT_AVATAR)
                   .build())
      .batch(batch)
      .email(STUDENT_EMAIL)
      .university(STUDENT_UNIVERSITY)
      .build();

    userWebResponse = UserWebResponse.builder()
      .id(STUDENT_ID)
      .name(STUDENT_NAME)
      .role(Role.STUDENT.name())
      .address(STUDENT_ADDRESS)
      .phone(STUDENT_PHONE)
      .avatar(URL_PREFIX + STUDENT_AVATAR)
      .avatarId(FILE_ID)
      .batch(BatchWebResponse.builder()
               .code(BATCH_CODE)
               .build())
      .email(STUDENT_EMAIL)
      .university(STUDENT_UNIVERSITY)
      .build();

    userWebResponseWithFinalPoint = new UserWebResponse();
    BeanUtils.copyProperties(userWebResponse, userWebResponseWithFinalPoint);
    userWebResponseWithFinalPoint.setFinalPoint(FINAL_POINT);

    reportDetail = ReportDetail
        .builder()
        .user(user)
        .build();

    report = Report.builder()
      .id(REPORT_ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .batch(batch)
      .students(Collections.singletonList(reportDetail))
      .build();

    report.setCreatedAt(CREATED_AT);

    reportWebRequest = ReportWebRequest.builder()
      .name(TITLE)
      .description(DESCRIPTION)
      .students(Collections.singletonList(STUDENT_ID))
      .build();

    reportWebResponse = ReportWebResponse.builder()
      .id(REPORT_ID)
      .name(TITLE)
      .description(DESCRIPTION)
      .batchCode(BATCH_CODE)
      .studentCount(1)
      .students(Collections.singletonList(userWebResponse))
      .build();

    List<Pair<User, Integer>> pairList = Collections.singletonList(
      Pair.of(user, FINAL_POINT));

    reportWebResponse.setUploadedDate(CREATED_AT);

    DATA_RESPONSE = ResponseHelper.toDataResponse(
      HttpStatus.OK, reportWebResponse);
    CREATED_DATA_RESPONSE = ResponseHelper.toDataResponse(
      HttpStatus.CREATED, reportWebResponse);
    PAGING_RESPONSE = ResponseHelper.toPagingResponse(HttpStatus.OK,
                                                      Collections.singletonList(
                                                        reportWebResponse),
                                                      paging
    );
    USER_PAGING_RESPONSE = ResponseHelper.toPagingResponse(HttpStatus.OK,
                                                           Collections.singletonList(
                                                             userWebResponseWithFinalPoint),
                                                           paging
    );
    when(reportService.findAllReport(BATCH_CODE, pageable)).thenReturn(
      new PageImpl<>(Collections.singletonList(report), pageable, 1));
    when(reportService.findAllStudentsAndFinalPointByBatch(BATCH_CODE,
                                                           pageable
    )).thenReturn(new PageImpl<>(pairList, pageable, 1));
    when(reportService.createReport(report)).thenReturn(report);
    when(reportService.updateReport(report)).thenReturn(report);
    when(reportService.findById(REPORT_ID)).thenReturn(report);
    when(requestMapper.toReport(reportWebRequest, BATCH_CODE)).thenReturn(
      report);
    when(requestMapper.toReport(reportWebRequest, REPORT_ID,
                                BATCH_CODE
    )).thenReturn(report);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(reportService, requestMapper, fileProperties);
  }

  @Test
  public void findAllReportByUsedAtNow() throws Exception {

    mockMvc.perform(get(
      "/api/scoring/batches/" + BATCH_CODE + "/judgings").cookie(cookies)
                      .param("page", "1")
                      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));
    verify(reportService).findAllReport(BATCH_CODE, pageable);
    verify(fileProperties).getUrlPrefix();
  }


  @Test
  public void findAllStudentsAndFinalPointWithinBatch() throws Exception {

    super.setCookie(Role.ADMIN);

    mockMvc.perform(get(
      "/api/scoring/batches/" + BATCH_CODE + "/judgings/students").cookie(
      cookies)
                      .param("role", Role.STUDENT.name()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(USER_PAGING_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(reportService).findAllStudentsAndFinalPointByBatch(
      BATCH_CODE, pageable);
  }

  @Test
  public void findOne() throws Exception {

    mockMvc.perform(get(
      "/api/scoring/batches/" + BATCH_CODE + "/judgings/" + REPORT_ID).cookie(
      cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));
    verify(reportService).findById(REPORT_ID);
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void createFinalJudging() throws Exception {

    mockMvc.perform(post(
      "/api/scoring/batches/" + BATCH_CODE + "/judgings").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(webRequestJacksonTester.write(reportWebRequest)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));
    verify(requestMapper).toReport(reportWebRequest, BATCH_CODE);
    verify(reportService).createReport(report);
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void updateFinalJudging() throws Exception {

    mockMvc.perform(put(
      "/api/scoring/batches/" + BATCH_CODE + "/judgings/" + REPORT_ID).cookie(
      cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(webRequestJacksonTester.write(reportWebRequest)
                                 .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));
    verify(requestMapper).toReport(reportWebRequest, REPORT_ID, BATCH_CODE);
    verify(reportService).updateReport(report);
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void deleteById() throws Exception {

    mockMvc.perform(delete(
      "/api/scoring/batches/" + BATCH_CODE + "/judgings/" + REPORT_ID).cookie(
      cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(
        ResponseHelper.toBaseResponse(HttpStatus.OK))
                                  .getJson()));
    verify(reportService).deleteById(REPORT_ID);
  }

}
