package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentRequestMapperTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final String ASSIGNMENT_BATCH = "2";
  private static final String ASSIGNMENT_ID = "assignment-id";
  private static final long ASSIGNMENT_DEADLINE = 1561520805;
  private Assignment assignment;
  private AssignmentWebRequest assignmentWebRequest;

  @Mock
  private WebRequestMapper requestMapper;

  @InjectMocks
  private AssignmentRequestMapper assignmentRequestMapper;

  @Mock
  private RequestValidator validator;

  @Before
  public void setUp() {
    assignment = Assignment
        .builder()
        .id(null)
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .deadline(ASSIGNMENT_DEADLINE)
            .batch(Batch.builder().code(ASSIGNMENT_BATCH).build())
        .build();

    assignmentWebRequest = AssignmentWebRequest
        .builder()
        .title(ASSIGNMENT_TITLE)
        .description(ASSIGNMENT_DESCRIPTION)
        .files(new ArrayList<>())
        .deadline(ASSIGNMENT_DEADLINE)
        .build();

    when(validator.validate(assignmentWebRequest))
        .thenReturn(assignmentWebRequest);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(requestMapper);
    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testToAssignmentFromStringDataJson() {
    Assignment actual = assignmentRequestMapper.toAssignment(assignmentWebRequest, ASSIGNMENT_BATCH);
    assertThat(actual.getDescription()).isEqualTo(assignment.getDescription());
    assertThat(actual.getTitle()).isEqualTo(assignment.getTitle());
    verify(validator).validate(assignmentWebRequest);
  }

  @Test
  public void testToAssignmentFromStringDataJsonFileExist() {
    assignmentWebRequest.setFiles(Collections.singletonList("file-id"));
    assignment.setFile(FileV2.builder().id("file-id").build());
    Assignment actual = assignmentRequestMapper.toAssignment(assignmentWebRequest, ASSIGNMENT_BATCH);
    assertThat(actual.getDescription()).isEqualTo(assignment.getDescription());
    assertThat(actual.getTitle()).isEqualTo(assignment.getTitle());
    verify(validator).validate(assignmentWebRequest);
  }

  @Test
  public void testToAssignmentFromStringDataJsonAndStringIdSuccess() {
    assignment.setId(ASSIGNMENT_ID);
    Assignment actual = assignmentRequestMapper.toAssignmentWithId(ASSIGNMENT_ID, assignmentWebRequest,
            ASSIGNMENT_BATCH);
    assertThat(actual.getDescription()).isEqualTo(assignment.getDescription());
    assertThat(actual.getTitle()).isEqualTo(assignment.getTitle());
    verify(validator).validate(assignmentWebRequest);
  }
}