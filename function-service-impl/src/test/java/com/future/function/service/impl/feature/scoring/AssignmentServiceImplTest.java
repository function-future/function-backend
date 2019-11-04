package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.impl.helper.CopyHelper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceImplTest {

  private static final Long DATE_NOW = LocalDate.now().atTime(23, 59)
      .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

  private static final String ASSIGNMENT_ID = "assignment-id";

  private static final String ASSIGNMENT_TITLE = "assignment-title";

  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";

  private static final long ASSIGNMENT_DEADLINE = new Date().getTime() + 150000;

  private static final String BATCH_CODE = "batchCode";

  private static final String BATCH_ID = "batchCode";

  private static final String USER_ID = "userId";

  private static final String ROOM_ID = "room-id";

  private static final String FILE_PATH = "file-path";

  private static final String FILE_ID = "file-id";

  private Assignment assignment;

  private Assignment assignment2;

  private FileV2 file;

  private Pageable pageable;

  private Batch batch;

  private Room room;

  private User student;

  private Page<Assignment> assignmentPage;

  private Page<Room> roomPage;

  private List<Assignment> assignmentList;

  @InjectMocks
  private AssignmentServiceImpl assignmentService;

  @Mock
  private AssignmentRepository assignmentRepository;

  @Mock
  private ResourceService resourceService;

  @Mock
  private BatchService batchService;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .id(BATCH_ID)
      .build();

    student = User.builder()
      .id(USER_ID)
      .build();

    file = FileV2.builder()
      .filePath(FILE_PATH)
      .id(FILE_ID)
      .build();

    assignment = Assignment.builder()
      .id(ASSIGNMENT_ID)
      .title(ASSIGNMENT_TITLE)
      .description(ASSIGNMENT_DESCRIPTION)
      .deadline(ASSIGNMENT_DEADLINE)
      .batch(batch)
      .file(file)
      .build();

    assignment2 = Assignment.builder().build();
    BeanUtils.copyProperties(assignment, assignment2, "id");
    assignment2.setDeadline(assignment2.getDeadline() - 1000000);

    room = Room.builder()
      .id(ROOM_ID)
      .build();

    pageable = new PageRequest(0, 10);

    assignmentList = new ArrayList<>();
    assignmentList.add(assignment);
    assignmentList.add(assignment2);

    assignmentPage = new PageImpl<>(assignmentList, pageable, 2);
    roomPage = new PageImpl<>(Collections.singletonList(room), pageable, 1);

    when(
      assignmentRepository.findByIdAndDeletedFalse(ASSIGNMENT_ID)).thenReturn(
      Optional.of(assignment));
    when(assignmentRepository.findAllByBatchAndDeletedFalseAndDeadlineAfterOrderByDeadlineDesc(batch, DATE_NOW, pageable))
        .thenReturn(assignmentPage);
    when(assignmentRepository.findAllByBatchAndDeletedFalseAndDeadlineBeforeOrderByDeadlineAsc(batch, DATE_NOW, pageable))
        .thenReturn(assignmentPage);
    when(assignmentRepository.save(assignment)).thenReturn(assignment);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
    when(batchService.getBatchById(BATCH_ID)).thenReturn(batch);
    when(resourceService.getFile(FILE_ID)).thenReturn(file);
    when(resourceService.markFilesUsed(Collections.singletonList(FILE_ID),
                                       true
    )).thenReturn(true);
    when(resourceService.markFilesUsed(Collections.singletonList(FILE_ID),
                                       false
    )).thenReturn(true);
  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(
      assignmentRepository, batchService, resourceService);
  }

  @Test
  public void testFindAllAssignmentWithPageable() {

    Page<Assignment> result = assignmentService.findAllByBatchCodeAndPageable(
      BATCH_CODE, pageable, Role.STUDENT, BATCH_ID, true);
    assertThat(result).isNotNull();
    assertThat(result.getContent()
                 .size()).isEqualTo(2);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).findAllByBatchAndDeletedFalseAndDeadlineAfterOrderByDeadlineDesc(batch, DATE_NOW, pageable);
  }

  @Test
  public void testFindAllAssignmentWithPageableAsAdmin() {

    Page<Assignment> result = assignmentService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.ADMIN, "", false);
    assertThat(result).isNotNull();
    assertThat(result.getContent()
        .size()).isEqualTo(2);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).findAllByBatchAndDeletedFalseAndDeadlineBeforeOrderByDeadlineAsc(batch, DATE_NOW, pageable);
  }

  @Test
  public void testFindAllAssignmentWithPageableAsAnotherBatchStudent() {

    catchException(() -> assignmentService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.STUDENT, "another-batch-id", true));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(batchService).getBatchByCode(BATCH_CODE);
  }

  @Test
  public void testFindAllAssignmentWithPageableAllAboveDeadline() {

    assignment2.setDeadline(assignment2.getDeadline() + 2000000);
    Page<Assignment> result = assignmentService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.STUDENT, BATCH_ID, true);
    assertThat(result).isNotNull();
    assertThat(result.getContent()
        .size()).isEqualTo(2);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).findAllByBatchAndDeletedFalseAndDeadlineAfterOrderByDeadlineDesc(batch, DATE_NOW, pageable);
  }

  @Test
  public void testFindAllAssignmentWithPageableAssignment1BelowDeadline() {

    assignment2.setDeadline(assignment2.getDeadline() + 2000000);
    assignment.setDeadline(assignment.getDeadline() - 3000000);
    List<Assignment> expected = Arrays.asList(assignment, assignment2);
    Page<Assignment> result = assignmentService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.STUDENT, BATCH_ID, true);
    assertThat(result).isNotNull();
    assertThat(result.getContent()
        .size()).isEqualTo(2);
    assertThat(result.getContent()).isEqualTo(expected);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).findAllByBatchAndDeletedFalseAndDeadlineAfterOrderByDeadlineDesc(batch, DATE_NOW, pageable);
  }

  @Test
  public void testFindAllAssignmentWithPageableAllAssignmentBelowDeadline() {

    assignment.setDeadline(assignment.getDeadline() - 3000000);
    Page<Assignment> result = assignmentService.findAllByBatchCodeAndPageable(
        BATCH_CODE, pageable, Role.STUDENT, BATCH_ID, true);
    assertThat(result).isNotNull();
    assertThat(result.getContent()
        .size()).isEqualTo(2);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).findAllByBatchAndDeletedFalseAndDeadlineAfterOrderByDeadlineDesc(batch, DATE_NOW, pageable);
  }

  @Test
  public void testFindByIdSuccess() {

    Assignment result = assignmentService.findById(assignment.getId(), Role.STUDENT, BATCH_ID);
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
  }

  @Test
  public void testFindByIdSuccessRoleAdmin() {

    Assignment result = assignmentService.findById(assignment.getId(), Role.ADMIN, "");
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
  }

  @Test
  public void testFindByIdFailedAnotherStudentBatchAccess() {

    catchException(() -> assignmentService
        .findById(assignment.getId(), Role.STUDENT, "another-batch-id"));
    assertThat(caughtException().getClass()).isEqualTo(ForbiddenException.class);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
  }

  @Test
  public void testFindByIdNull() {

    catchException(() -> assignmentService.findById(null, Role.STUDENT, BATCH_ID));
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    verifyZeroInteractions(assignmentRepository);
  }

  @Test
  public void testDeleteByIdSuccess() {

    assignmentService.deleteById(assignment.getId());
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    assignment.setDeleted(true);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testDeleteByIdSuccessWithFile() {

    assignment.setFile(FileV2.builder()
                         .id(FILE_ID)
                         .build());
    assignmentService.deleteById(assignment.getId());
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    assignment.setDeleted(true);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testDeleteByIdSuccessWithFileIdBlank() {

    assignment.setFile(FileV2.builder()
                         .id("")
                         .build());
    assignmentService.deleteById(assignment.getId());
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
    assignment.setDeleted(true);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testDeleteByIdSuccessWithFileNull() {

    assignment.setFile(null);
    assignmentService.deleteById(assignment.getId());
    verify(assignmentRepository).findByIdAndDeletedFalse(assignment.getId());
    assignment.setDeleted(true);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testCreateAssignmentSuccess() {

    Assignment actual = assignmentService.createAssignment(assignment);
    assertThat(actual.getFile()).isEqualTo(file);
    assertThat(actual).isEqualTo(assignment);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), true);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testCreateAssignmentNullFile() {

    assignment.setFile(null);
    Assignment actual = assignmentService.createAssignment(assignment);
    assertThat(actual.getFile()).isEqualTo(null);
    assertThat(actual).isEqualTo(assignment);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testUpdateAssignmentSuccess() {

    Assignment assignmentWithFile = assignment;
    assignmentWithFile.setFile(file);
    when(assignmentRepository.save(assignmentWithFile)).thenReturn(
      assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignment);
    assertThat(actual.getFile()).isEqualTo(file);
    assertThat(actual).isEqualTo(assignment);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), true);
    verify(assignmentRepository).save(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(
      assignmentWithFile.getId());
  }

  @Test
  public void testUpdateAssignmentNoFileOnRequestAndDBSuccess() {

    Assignment assignmentWithNoFile = new Assignment();
    CopyHelper.copyProperties(assignment, assignmentWithNoFile);
    assignmentWithNoFile.setId(ASSIGNMENT_ID);
    assignmentWithNoFile.setFile(null);
    assignment.setFile(null);
    when(assignmentRepository.save(assignmentWithNoFile)).thenReturn(
      assignmentWithNoFile);
    Assignment actual = assignmentService.updateAssignment(
      assignmentWithNoFile);
    assertThat(actual.getFile()).isEqualTo(null);
    assertThat(actual).isEqualTo(assignmentWithNoFile);
    verify(assignmentRepository).save(assignmentWithNoFile);
    verify(assignmentRepository).findByIdAndDeletedFalse(
      assignmentWithNoFile.getId());
  }

  @Test
  public void testUpdateAssignmentWithFileOnRequestSuccess() {

    Assignment assignmentWithFile = new Assignment();
    CopyHelper.copyProperties(assignment, assignmentWithFile);
    assignmentWithFile.setId(ASSIGNMENT_ID);
    assignmentWithFile.setFile(FileV2.builder()
                                 .id("id")
                                 .build());
    when(assignmentRepository.save(assignmentWithFile)).thenReturn(
      assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignmentWithFile);
    assertThat(actual.getFile()
                 .getId()).isEqualTo("id");
    assertThat(actual).isEqualTo(assignmentWithFile);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    verify(resourceService).markFilesUsed(
      Collections.singletonList("id"), true);
    verify(assignmentRepository).save(assignmentWithFile);
    verify(assignmentRepository).findByIdAndDeletedFalse(
      assignmentWithFile.getId());
  }

  @Test
  public void testUpdateAssignmentNoFileOnRequestExistOnDBSuccess() {

    Assignment assignmentWithNoFile = new Assignment();
    CopyHelper.copyProperties(assignment, assignmentWithNoFile);
    assignmentWithNoFile.setId(ASSIGNMENT_ID);
    assignmentWithNoFile.setFile(null);
    when(assignmentRepository.save(assignmentWithNoFile)).thenReturn(
      assignmentWithNoFile);
    Assignment actual = assignmentService.updateAssignment(
      assignmentWithNoFile);
    assertThat(actual).isEqualTo(assignment);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    verify(assignmentRepository).save(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(
      assignmentWithNoFile.getId());
  }

  @Test
  public void testUpdateAssignmentDifferentBatchAndFileSuccess() {

    Assignment assignmentWithFile = new Assignment();
    FileV2 anotherFile = FileV2.builder()
      .id("id")
      .build();
    BeanUtils.copyProperties(assignment, assignmentWithFile);
    assignmentWithFile.setFile(anotherFile);
    when(resourceService.getFile("id")).thenReturn(anotherFile);
    when(resourceService.markFilesUsed(Collections.singletonList("id"),
                                       true
    )).thenReturn(true);
    when(assignmentRepository.findByIdAndDeletedFalse(
      assignmentWithFile.getId())).thenReturn(Optional.of(assignment));
    when(assignmentRepository.save(assignmentWithFile)).thenReturn(
      assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignmentWithFile);
    assertThat(actual.getFile()).isEqualTo(anotherFile);
    assertThat(actual).isEqualTo(assignmentWithFile);
    verify(resourceService).getFile("id");
    verify(resourceService).markFilesUsed(
      Collections.singletonList("id"), true);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    verify(assignmentRepository).save(assignmentWithFile);
    verify(assignmentRepository).findByIdAndDeletedFalse(
      assignmentWithFile.getId());
  }

  @Test
  public void testUpdateAssignmentDifferentBatchAndFileSuccessFileFromDBNull() {

    Assignment assignmentWithFile = new Assignment();
    FileV2 anotherFile = FileV2.builder()
      .id("id")
      .build();
    assignment.setFile(null);
    BeanUtils.copyProperties(assignment, assignmentWithFile);
    assignmentWithFile.setFile(anotherFile);
    when(resourceService.getFile("id")).thenReturn(anotherFile);
    when(resourceService.markFilesUsed(Collections.singletonList("id"),
                                       true
    )).thenReturn(true);
    when(assignmentRepository.findByIdAndDeletedFalse(
      assignmentWithFile.getId())).thenReturn(Optional.of(assignment));
    when(assignmentRepository.save(assignmentWithFile)).thenReturn(
      assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignmentWithFile);
    assertThat(actual.getFile()).isEqualTo(anotherFile);
    assertThat(actual).isEqualTo(assignmentWithFile);
    verify(resourceService).getFile("id");
    verify(resourceService).markFilesUsed(
      Collections.singletonList("id"), true);
    verify(assignmentRepository).save(assignmentWithFile);
    verify(assignmentRepository).findByIdAndDeletedFalse(
      assignmentWithFile.getId());
  }

  @Test
  public void copyAssignmentTest() {

    when(assignmentRepository.findByIdAndDeletedFalse("id")).thenReturn(
      Optional.of(assignment));
    Batch anotherBatch = Batch.builder()
      .code("CODE")
      .id("ID")
      .build();
    when(batchService.getBatchByCode("CODE")).thenReturn(anotherBatch);
    Assignment anotherAssignment = Assignment.builder()
      .build();
    BeanUtils.copyProperties(assignment, anotherAssignment, "id");
    anotherAssignment.setBatch(anotherBatch);
    when(assignmentRepository.save(any(Assignment.class))).thenReturn(
      anotherAssignment);
    Assignment actual = assignmentService.copyAssignment("id", "CODE");
    assertThat(actual.getBatch()
                 .getCode()).isEqualTo("CODE");
    assertThat(actual.getId()).isNotEqualTo("id");
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), true);
    verify(resourceService).getFile(FILE_ID);
    verify(batchService, times(2)).getBatchByCode("CODE");
    verify(assignmentRepository).findByIdAndDeletedFalse("id");
    verify(assignmentRepository).save(any(Assignment.class));
  }

}
