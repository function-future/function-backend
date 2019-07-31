package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.AssignmentRequestMapper;
import com.future.function.web.mapper.response.scoring.AssignmentResponseMapper;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import com.future.function.web.model.request.scoring.CopyAssignmentWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.AssignmentWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest extends TestHelper {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String BATCH_CODE = "3";
  private static final String ASSIGNMENT_FILE_PATH = "assignment-file-path";
  private static String ASSIGNMENT_ID = UUID.randomUUID().toString();
  private Pageable pageable;
  private Assignment assignment;
  private AssignmentWebRequest assignmentWebRequest;
  private CopyAssignmentWebRequest copyAssignmentWebRequest;
  private List<Assignment> assignmentList;
  private Page<Assignment> assignmentPage;

  private DataResponse<AssignmentWebResponse> DATA_RESPONSE;
  private DataResponse<AssignmentWebResponse> CREATED_DATA_RESPONSE;

  private PagingResponse<AssignmentWebResponse> PAGING_RESPONSE;

  private BaseResponse BASE_RESPONSE;

  private JacksonTester<AssignmentWebRequest> assignmentWebRequestJacksonTester;

  private JacksonTester<CopyAssignmentWebRequest> copyAssignmentWebRequestJacksonTester;

  @MockBean
  private AssignmentService assignmentService;

  @MockBean
  private AssignmentRequestMapper assignmentRequestMapper;

  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.ADMIN);
    assignment = Assignment
            .builder()
            .id(ASSIGNMENT_ID)
            .title(ASSIGNMENT_TITLE)
            .description(ASSIGNMENT_DESCRIPTION)
            .deadline(ASSIGNMENT_DEADLINE)
            .file(FileV2.builder().id("file-id").build())
            .batch(Batch.builder().code(BATCH_CODE).build())
            .build();

    assignment.setCreatedAt(ASSIGNMENT_DEADLINE);

    assignmentWebRequest = AssignmentWebRequest.builder()
        .deadline(ASSIGNMENT_DEADLINE)
        .description(ASSIGNMENT_DESCRIPTION)
        .title(ASSIGNMENT_TITLE)
        .files(Collections.singletonList("file-id"))
      .build();

    copyAssignmentWebRequest = CopyAssignmentWebRequest
        .builder()
            .batchCode("BATCH-3")
        .assignmentId(ASSIGNMENT_ID)
        .build();

    assignmentList = new ArrayList<>();
    assignmentList.add(assignment);

    pageable = new PageRequest(0, 10);

    assignmentPage = new PageImpl<>(assignmentList, pageable, 10);

    ASSIGNMENT_ID = assignment.getId();

    DATA_RESPONSE = AssignmentResponseMapper
            .toAssignmentDataResponse(this.assignment);

    CREATED_DATA_RESPONSE = AssignmentResponseMapper
            .toAssignmentDataResponse(HttpStatus.CREATED, this.assignment);

    PAGING_RESPONSE = AssignmentResponseMapper
            .toAssignmentsPagingResponse(assignmentPage);

    BASE_RESPONSE = ResponseHelper.toBaseResponse(HttpStatus.OK);

    when(assignmentService.findAllByBatchCodeAndPageable(BATCH_CODE, pageable))
            .thenReturn(assignmentPage);
    when(assignmentService.copyAssignment(ASSIGNMENT_ID, "BATCH-3")).thenReturn(assignment);
    when(assignmentService.createAssignment(assignment))
            .thenReturn(assignment);
    when(assignmentService.updateAssignment(assignment))
            .thenReturn(assignment);
    when(assignmentService.findById(ASSIGNMENT_ID))
            .thenReturn(assignment);
      when(assignmentRequestMapper.toAssignment(assignmentWebRequest, BATCH_CODE))
            .thenReturn(assignment);
      when(assignmentRequestMapper.toAssignmentWithId(ASSIGNMENT_ID, assignmentWebRequest, BATCH_CODE))
            .thenReturn(assignment);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(assignmentService);
    verifyNoMoreInteractions(assignmentRequestMapper);
  }

  @Test
  public void testCopyAssignmentByCopyAssignmentWebRequest() throws Exception {
    mockMvc.perform(
        post("/api/scoring/batches/" + BATCH_CODE + "/assignments/copy")
            .cookie(cookies)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(copyAssignmentWebRequestJacksonTester.write(copyAssignmentWebRequest).getJson()))
        .andExpect(status().isCreated())
        .andExpect(content().json(
            dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
            .getJson()));
    verify(assignmentService).copyAssignment(ASSIGNMENT_ID, "BATCH-3");
  }

  @Test
  public void testFindAssignmentByIdDataResponseAssignment() throws Exception {

    mockMvc.perform(
        get("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID)
            .cookie(cookies))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    dataResponseJacksonTester.write(DATA_RESPONSE)
                            .getJson()));

    verify(assignmentService).findById(ASSIGNMENT_ID);
    verifyZeroInteractions(assignmentRequestMapper);

  }

  @Test
  public void testDeleteAssignmentByIdBaseResponseOk() throws Exception {
    mockMvc.perform(
        delete("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID)
            .cookie(cookies))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    baseResponseJacksonTester.write(BASE_RESPONSE)
                            .getJson()));

    verify(assignmentService).deleteById(ASSIGNMENT_ID);
    verifyZeroInteractions(assignmentRequestMapper);
  }

  @Test
  public void testCreateAssignment() throws Exception {
    mockMvc.perform(
            post("/api/scoring/batches/" + BATCH_CODE + "/assignments")
                .cookie(cookies)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(assignmentWebRequestJacksonTester.write(assignmentWebRequest).getJson()))
            .andExpect(status().isCreated())
            .andExpect(content().json(
                dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                .getJson()));

    verify(assignmentService).createAssignment(assignment);
      verify(assignmentRequestMapper).toAssignment(assignmentWebRequest, BATCH_CODE);
  }

  @Test
  public void testUpdateAssignmentWithRequest() throws Exception {
    mockMvc.perform(
            put("/api/scoring/batches/" + BATCH_CODE + "/assignments/" + ASSIGNMENT_ID)
                .cookie(cookies)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(assignmentWebRequestJacksonTester.write(assignmentWebRequest).getJson()))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    dataResponseJacksonTester.write(DATA_RESPONSE)
                            .getJson()));

    verify(assignmentService).updateAssignment(assignment);
      verify(assignmentRequestMapper).toAssignmentWithId(ASSIGNMENT_ID, assignmentWebRequest, BATCH_CODE);
  }

  @Test
  public void testFindAllAssignmentWithNoPagingParameters() throws Exception {
    mockMvc.perform(
        get("/api/scoring/batches/" + BATCH_CODE + "/assignments")
            .cookie(cookies))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    pagingResponseJacksonTester.write(PAGING_RESPONSE)
                            .getJson()));

    verify(assignmentService).findAllByBatchCodeAndPageable(BATCH_CODE, pageable);
    verifyZeroInteractions(assignmentRequestMapper);
  }

  @Test
  public void testFindAllAssignmentWithPagingParameters() throws Exception {
    mockMvc.perform(
            get("/api/scoring/batches/" + BATCH_CODE + "/assignments")
                .cookie(cookies)
                .param("page", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json(
                    pagingResponseJacksonTester.write(PAGING_RESPONSE)
                            .getJson()));

    verify(assignmentService).findAllByBatchCodeAndPageable(BATCH_CODE, pageable);
    verifyZeroInteractions(assignmentRequestMapper);
  }
}
