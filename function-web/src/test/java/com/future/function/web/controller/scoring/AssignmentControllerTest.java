package com.future.function.web.controller.scoring;

import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.web.mapper.request.scoring.AssignmentRequestMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final String ASSIGNMENT_QUESTION = "assignment-question";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String ASSIGNMENT_BATCH = "[2, 3]";
  private static final String ASSIGNMENT_FILE_PATH = "assignment-file-path";
  private static final String ASSIGNMENT_FILE = "file";
  private static final String ASSIGNMENT_CREATE_REQUEST_JSON =
          "{\n" + "\"title\": \"" + ASSIGNMENT_TITLE + "\",\n" + "    \"description\": \"" +
                  ASSIGNMENT_DESCRIPTION + "\",\n" + "    \"question\": \"" + ASSIGNMENT_QUESTION + "\",\n" +
                  "    \"deadline\": " + ASSIGNMENT_DEADLINE + ",\n" + "    \"batch\": " + ASSIGNMENT_BATCH +
                  "}";
  private static String ASSIGNMENT_ID;
  private static final String ASSIGNMENT_UPDATE_REQUEST_JSON =
          "{\n" + "\"id\": \"" + ASSIGNMENT_ID + "\",\n" + "\"title\": \"" + ASSIGNMENT_TITLE + "\",\n" +
                  "    \"description\": \"" + ASSIGNMENT_DESCRIPTION + "\",\n" + "    \"question\": \"" +
                  ASSIGNMENT_QUESTION + "\",\n" + "    \"deadline\": " + ASSIGNMENT_DEADLINE + ",\n" +
                  "    \"batch\": " + ASSIGNMENT_BATCH + "}";
  private File file;
  private Pageable pageable;
  private Assignment assignment;
  private Assignment assignmentWithFile;
  private MockMultipartFile multipartFile;
  private List<Assignment> assignmentList;
  private Page<Assignment> assignmentPage;
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AssignmentService assignmentService;

  @MockBean
  private AssignmentRequestMapper assignmentRequestMapper;

  @Before
  public void setUp() throws Exception {
    assignment = Assignment
            .builder()
            .title(ASSIGNMENT_TITLE)
            .description(ASSIGNMENT_DESCRIPTION)
            .deadline(ASSIGNMENT_DEADLINE)
            .question(ASSIGNMENT_QUESTION)
            .build();

    assignmentList = new ArrayList<>();
    assignmentList.add(assignment);

    pageable = new PageRequest(0, 10);

    assignmentPage = new PageImpl<>(assignmentList, pageable, 10);

    file = File
            .builder()
            .filePath(ASSIGNMENT_FILE_PATH)
            .build();

    ASSIGNMENT_ID = assignment.getId();

    assignmentWithFile = new Assignment();
    BeanUtils.copyProperties(assignment, assignmentWithFile);
    assignmentWithFile.setFile(file);

    multipartFile = new MockMultipartFile(ASSIGNMENT_FILE, new byte[]{});

    when(assignmentService.findAllByPageableAndFilterAndSearch(pageable, , ))
            .thenReturn(assignmentPage);
    when(assignmentService.createAssignment(assignment, multipartFile))
            .thenReturn(assignmentWithFile);
    when(assignmentService.createAssignment(assignment, null))
            .thenReturn(assignment);
    when(assignmentService.updateAssignment(assignment, null))
            .thenReturn(assignment);
    when(assignmentService.findById(ASSIGNMENT_ID))
            .thenReturn(assignment);
    when(assignmentRequestMapper.toAssignment(ASSIGNMENT_CREATE_REQUEST_JSON))
            .thenReturn(assignment);
    when(assignmentRequestMapper.toAssignment(ASSIGNMENT_UPDATE_REQUEST_JSON))
            .thenReturn(assignment);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(assignmentService);
    verifyNoMoreInteractions(assignmentRequestMapper);
  }

  @Test
  public void testFindAssignmentByIdDataResponseAssignment() throws Exception {

    MockHttpServletResponse response = mockMvc.perform(
            get("/api/scoring/assignments/" + ASSIGNMENT_ID)
    )
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(assignmentService).findById(ASSIGNMENT_ID);
    verifyZeroInteractions(assignmentRequestMapper);

  }

  @Test
  public void testDeleteAssignmentByIdBaseResponseOk() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
            delete("/api/scoring/assignments/" + ASSIGNMENT_ID)
    )
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    verify(assignmentService).deleteById(ASSIGNMENT_ID);
    verifyZeroInteractions(assignmentRequestMapper);
  }

  @Test
  public void testCreateAssignmentWithRequestJsonAndMultipartFile() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
            fileUpload("/api/scoring/assignments")
                    .file(multipartFile)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("data", ASSIGNMENT_CREATE_REQUEST_JSON)
    )
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).isNotBlank();
    verify(assignmentService).createAssignment(assignment, multipartFile);
    verify(assignmentRequestMapper).toAssignment(ASSIGNMENT_CREATE_REQUEST_JSON);
  }

  @Test
  public void testCreateAssignmentWithRequestJsonWithoutMultipartFile() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
            post("/api/scoring/assignments")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("data", ASSIGNMENT_CREATE_REQUEST_JSON)
                    .param("file", "")
    )
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).isNotBlank();
    verify(assignmentService).createAssignment(assignment, null);
    verify(assignmentRequestMapper).toAssignment(ASSIGNMENT_CREATE_REQUEST_JSON);
  }

  @Test
  public void testUpdateAssignmentWithRequestJsonWithoutMultipartFile() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
            put("/api/scoring/assignments")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("data", ASSIGNMENT_UPDATE_REQUEST_JSON)
                    .param("file", "")
    )
            .andReturn()
            .getResponse();


    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();
    verify(assignmentService).updateAssignment(assignment, null);
    verify(assignmentRequestMapper).toAssignment(ASSIGNMENT_UPDATE_REQUEST_JSON);
  }

  @Test
  public void testFindAllAssignmentWithPagingParameters() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
            get("/api/scoring/assignments")
                    .param("page", "1")
                    .param("size", "10")
    )
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotBlank();

    verify(assignmentService).findAllByPageableAndFilterAndSearch(eq(pageable), , );
    verifyZeroInteractions(assignmentRequestMapper);
  }
}