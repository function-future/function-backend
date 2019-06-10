package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.repository.feature.scoring.AssignmentRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceImplTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final String ASSIGNMENT_QUESTION = "assignment-question";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();


  private static final String ERROR_MSG_NOT_FOUND = "Assignment Not Found";

  private static final String FILE_PATH = "file-path";
  private static final String FILE_ID = "file-id";

  private static final String STRING_EMPTY = "";

  private Assignment assignment;
    private FileV2 file;
  private MockMultipartFile multipartFile;
  private Pageable pageable;
  private Page<Assignment> assignmentPage;
  private List<Assignment> assignmentList;

  @InjectMocks
  private AssignmentServiceImpl assignmentService;

  @Mock
  private AssignmentRepository assignmentRepository;

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

      file = FileV2
            .builder()
            .filePath(FILE_PATH)
            .id(FILE_ID)
            .build();
    when(assignmentRepository.findByIdAndDeletedFalse(assignment.getId()))
            .thenReturn(Optional.of(assignment));
    when(assignmentRepository.findAll(pageable))
            .thenReturn(assignmentPage);
    when(assignmentRepository.save(assignment))
            .thenReturn(assignment);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(assignmentRepository);
  }

  @Test
  public void testFindAllAssignmentWithPageable() {
    Page<Assignment> result = assignmentService.findAllByPageableAndFilterAndSearch(pageable, STRING_EMPTY, STRING_EMPTY);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isEqualTo(1);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(assignmentRepository).findAll(pageable);
  }

  @Test
  public void testFindByIdSuccess() {
    Assignment result = assignmentService.findById(assignment.getId());
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
  }

  @Test
  public void testFindByIdNull() {
    catchException(() -> assignmentService.findById(null));
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(ERROR_MSG_NOT_FOUND);
    verifyZeroInteractions(assignmentRepository);
  }

  @Test
  public void testDeleteByIdSuccess() {
    assignmentService.deleteById(assignment.getId());
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
    assignment.setDeleted(true);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testCreateAssignmentSuccess() {
    Assignment actual = assignmentService.createAssignment(assignment, multipartFile);
//    assertThat(actual.getFile()).isEqualTo(file);
    assertThat(actual).isEqualTo(assignment);
//    verify(fileService).getFile(eq(FILE_ID));
//    verify(fileService).storeFile(multipartFile, FileOrigin.ASSIGNMENT);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testCreateAssignmentFailedStoreFile() {
    Assignment actual = assignmentService.createAssignment(assignment, null);
    assertThat(actual.getFile()).isEqualTo(null);
    assertThat(actual).isEqualTo(assignment);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testUpdateAssignmentSuccess() {
    Assignment assignmentWithFile = assignment;
    assignmentWithFile.setFile(file);
    when(assignmentRepository.save(assignmentWithFile))
            .thenReturn(assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignment, multipartFile);
    assertThat(actual.getFile()).isEqualTo(file);
    assertThat(actual).isEqualTo(assignment);
//    verify(fileService).getFile(eq(FILE_ID));
//    verify(fileService).storeFile(multipartFile, FileOrigin.ASSIGNMENT);
    verify(assignmentRepository).save(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignmentWithFile.getId());
  }

  @Test
  public void testUpdateAssignmentFailedStoreFile() {
    Assignment actual = assignmentService.updateAssignment(assignment, null);
    assertThat(actual.getFile()).isEqualTo(null);
    assertThat(actual).isEqualTo(assignment);
    verify(assignmentRepository).save(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
  }
}