package com.future.function.web.mapper.request.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import java.io.IOException;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentRequestMapperTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final String ASSIGNMENT_QUESTION = "assignment-question";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String ASSIGNMENT_BATCH = "[2, 3]";
  private static final String ASSIGNMENT_FILE_PATH = "assignment-file-path";
  private static final String ASSIGNMENT_FILE = "file";
  private static final String NULL_VALUE = null;
  private static final String STRING_EMPTY = "";
  private static final String BAD_REQUEST_EXCEPTION_MSG = "Bad Request";
  private static final String ASSIGNMENT_REQUEST_JSON =
          "{\n" + "\"title\": \"" + ASSIGNMENT_TITLE + "\",\n" + "    \"description\": \"" +
                  ASSIGNMENT_DESCRIPTION + "\",\n" + "    \"question\": \"" + ASSIGNMENT_QUESTION + "\",\n" +
                  "    \"deadline\": " + ASSIGNMENT_DEADLINE + ",\n" + "    \"batch\": " + ASSIGNMENT_BATCH +
                  "}";
  private static String ASSIGNMENT_ID;
  private Assignment assignment;
  private AssignmentWebRequest assignmentWebRequest;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private AssignmentRequestMapper assignmentRequestMapper;

  @Mock
  private ObjectValidator validator;

  @Before
  public void setUp() throws Exception {
    assignment = Assignment
            .builder()
            .id(null)
            .title(ASSIGNMENT_TITLE)
            .description(ASSIGNMENT_DESCRIPTION)
            .deadline(ASSIGNMENT_DEADLINE)
            .question(ASSIGNMENT_QUESTION)
            .build();

    assignmentWebRequest = AssignmentWebRequest
            .builder()
            .title(ASSIGNMENT_TITLE)
            .description(ASSIGNMENT_DESCRIPTION)
            .deadline(ASSIGNMENT_DEADLINE)
            .question(ASSIGNMENT_QUESTION)
            .build();

    when(validator.validate(assignment))
            .thenReturn(assignment);
    when(objectMapper.readValue(ASSIGNMENT_REQUEST_JSON, AssignmentWebRequest.class))
            .thenReturn(assignmentWebRequest);
    when(objectMapper.readValue(STRING_EMPTY, AssignmentWebRequest.class))
            .thenThrow(new IOException());
    when(objectMapper.readValue(NULL_VALUE, AssignmentWebRequest.class))
            .thenThrow(new IOException());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testToAssignmentFromStringDataJson() throws IOException {
    Assignment actual = assignmentRequestMapper.toAssignment(ASSIGNMENT_REQUEST_JSON);
    assertThat(actual).isEqualTo(assignment);

    verify(validator).validate(assignment);
    verify(objectMapper).readValue(ASSIGNMENT_REQUEST_JSON, AssignmentWebRequest.class);
  }

  @Test
  public void testToAssignmentFromStringDataJsonBlank() throws IOException {
    catchException(() -> assignmentRequestMapper.toAssignment(STRING_EMPTY));
    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
    assertThat(caughtException().getMessage()).isEqualTo(BAD_REQUEST_EXCEPTION_MSG);
    verify(objectMapper).readValue(STRING_EMPTY, AssignmentWebRequest.class);
    verifyZeroInteractions(validator);
  }

  @Test
  public void testToAssignmentFromStringDataJsonNull() throws IOException {
    catchException(() -> assignmentRequestMapper.toAssignment(NULL_VALUE));
    assertThat(caughtException().getClass()).isEqualTo(BadRequestException.class);
    assertThat(caughtException().getMessage()).isEqualTo(BAD_REQUEST_EXCEPTION_MSG);
    verify(objectMapper).readValue(NULL_VALUE, AssignmentWebRequest.class);
    verifyZeroInteractions(validator);
  }
}