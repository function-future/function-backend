package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.scoring.AssignmentWebResponse;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Ignore
public class AssignmentResponseMapperTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String BATCH_CODE = "batch-code";
  private static final String FILE_URl = "file-url";
  private Paging paging;
  private Pageable pageable;
  private Assignment assignment;
  private Batch batch;
  private FileV2 fileV2;
  private List<Assignment> assignmentList;
  private Page<Assignment> assignmentPage;
  private AssignmentWebResponse assignmentWebResponse;

  private DataResponse<AssignmentWebResponse> assignmentWebResponseDataResponse;
  private PagingResponse<AssignmentWebResponse> assignmentWebResponsePagingResponse;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder().code(BATCH_CODE).build();
    fileV2 = FileV2.builder().fileUrl(FILE_URl).build();

    assignment = Assignment
        .builder()
        .id(null)
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .deadline(ASSIGNMENT_DEADLINE)
        .batch(batch)
        .file(fileV2)
        .build();

    assignmentWebResponse = AssignmentWebResponse
        .builder()
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .deadline(ASSIGNMENT_DEADLINE)
        .batchCode(BATCH_CODE)
        .file(FILE_URl)
        .build();

    assignmentWebResponseDataResponse = DataResponse
        .<AssignmentWebResponse>builder()
        .data(assignmentWebResponse)
        .code(HttpStatus.OK.value())
        .status(ResponseHelper.toProperStatusFormat(HttpStatus.OK.getReasonPhrase()))
        .build();

    pageable = new PageRequest(0, 10);

    assignmentList = new ArrayList<>();
    assignmentList.add(assignment);

    assignmentPage = new PageImpl<>(assignmentList, pageable, 10);

    paging = Paging
        .builder()
        .page(assignmentPage.getNumber())
        .size(assignmentPage.getSize())
        .totalRecords(assignmentPage.getTotalElements())
        .build();

    assignmentWebResponsePagingResponse = PagingResponse
        .<AssignmentWebResponse>builder()
        .data(Collections.singletonList(assignmentWebResponse))
        .code(HttpStatus.OK.value())
        .status(ResponseHelper.toProperStatusFormat(HttpStatus.OK.getReasonPhrase()))
        .paging(paging)
        .build();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testToAssignmentDataResponseSuccess() {
    DataResponse<AssignmentWebResponse> actual = AssignmentResponseMapper.toAssignmentDataResponse(assignment);
    Assertions.assertThat(actual.getData()).isEqualTo(assignmentWebResponseDataResponse.getData());
    Assertions.assertThat(actual.getCode()).isEqualTo(assignmentWebResponseDataResponse.getCode());
    Assertions.assertThat(actual.getStatus()).isEqualTo(assignmentWebResponseDataResponse.getStatus());
  }

  @Test
  public void testToAssignmentDataResponseWithHttpStatusSuccess() {
    assignmentWebResponseDataResponse.setCode(HttpStatus.CREATED.value());
    assignmentWebResponseDataResponse.setStatus(ResponseHelper.toProperStatusFormat(HttpStatus.CREATED.getReasonPhrase()));
    DataResponse<AssignmentWebResponse> actual = AssignmentResponseMapper
        .toAssignmentDataResponse(HttpStatus.CREATED, assignment);
    Assertions.assertThat(actual.getData()).isEqualTo(assignmentWebResponseDataResponse.getData());
    Assertions.assertThat(actual.getCode()).isEqualTo(assignmentWebResponseDataResponse.getCode());
    Assertions.assertThat(actual.getStatus()).isEqualTo(assignmentWebResponseDataResponse.getStatus());
  }

  @Test
  public void testToAssignmentPagingResponseSuccess() {
    PagingResponse<AssignmentWebResponse> actual = AssignmentResponseMapper.toAssignmentsPagingResponse(assignmentPage);
    Assertions.assertThat(actual).isEqualTo(assignmentWebResponsePagingResponse);
    Assertions.assertThat(actual.getData()).isEqualTo(assignmentWebResponsePagingResponse.getData());
    Assertions.assertThat(actual.getCode()).isEqualTo(assignmentWebResponsePagingResponse.getCode());
    Assertions.assertThat(actual.getStatus()).isEqualTo(assignmentWebResponsePagingResponse.getStatus());
    Assertions.assertThat(actual.getPaging()).isEqualTo(assignmentWebResponsePagingResponse.getPaging());
  }
}