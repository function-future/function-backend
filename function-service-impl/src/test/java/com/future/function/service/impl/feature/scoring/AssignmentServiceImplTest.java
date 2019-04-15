package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.api.feature.scoring.AssignmentService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceImplTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final String ASSIGNMENT_QUESTION = "assignment-question";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();


  private static final String ERROR_MSG_NOT_FOUND = "Assignment Not Found";

  private static final String FILE_PATH = "file-path";
  private static final String FILE_ID = "file-id";

  private Assignment assignment;
  private File file;
  private MockMultipartFile multipartFile;
  private Pageable pageable;
  private Page<Assignment> assignmentPage;
  private List<Assignment> assignmentList;

  @InjectMocks
  private AssignmentService assignmentService;

  @Mock
  private AssignmentRepository assignmentRepository;

  @Mock
  private FileService fileService;

  @Before
  public void setUp() throws Exception {
    assignment = Assignment
            .builder()
            .title(ASSIGNMENT_TITLE)
            .description(ASSIGNMENT_DESCRIPTION)
            .question(ASSIGNMENT_QUESTION)
            .deadline(ASSIGNMENT_DEADLINE)
            .build();

    multipartFile = new MockMultipartFile("file", new byte[]{});

    pageable = new PageRequest(0, 10);

    assignmentList = new ArrayList<>();
    assignmentList.add(assignment);

    assignmentPage = new PageImpl<>(assignmentList, pageable, 10);

    file = File
            .builder()
            .filePath(FILE_PATH)
            .id(FILE_ID)
            .build();

    when(fileService.storeFile(multipartFile, FileOrigin.ASSIGNMENT))
            .thenReturn(file);
    when(fileService.getFile(FILE_ID))
            .thenReturn(file);
    when(assignmentRepository.findByIdAndDeletedFalse(assignment.getId()))
            .thenReturn(Optional.of(assignment));
    when(assignmentRepository.findAll(pageable))
            .thenReturn(assignmentPage);
    when(assignmentRepository.save(assignment))
            .thenReturn(assignment);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testFindAllAssignmentWithPageable() {
    Page<Assignment> result = assignmentService.findAllBuPageable(pageable);
    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(assignmentRepository).findAll(eq(pageable));
  }

  @Test
  public void testFindByIdSuccess() {
    Assignment result = assignmentService.findById(assignment.getId());
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(eq(assignment.getId()));
  }

  @Test
  public void testFindByIdNull() {
    assertThat(assignmentService.findById(null)).isEqualTo(new NotFoundException(ERROR_MSG_NOT_FOUND));
  }

  @Test
  public void testDeleteByIdSuccess() {
    assignmentService.deleteById(assignment.getId());
    verify(assignmentRepository).findByIdAndDeletedFalse(eq(assignment.getId()));
    verify(assignmentRepository).delete(eq(assignment));
  }
}