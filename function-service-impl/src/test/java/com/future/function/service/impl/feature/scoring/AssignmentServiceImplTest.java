package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.scoring.RoomService;
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

import java.util.*;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceImplTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String BATCH_CODE = "batchCode";

  private static final String ROOM_ID = "room-id";

  private static final String ERROR_MSG_NOT_FOUND = "Assignment Not Found";

  private static final String FILE_PATH = "file-path";
  private static final String FILE_ID = "file-id";

  private Assignment assignment;
  private FileV2 file;
  private Pageable pageable;
  private Batch batch;
  private Room room;
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
  private RoomService roomService;

  @Mock
  private BatchService batchService;

  @Before
  public void setUp() throws Exception {

    batch = Batch.builder().code(BATCH_CODE).build();

    file = FileV2
            .builder()
            .filePath(FILE_PATH)
            .id(FILE_ID)
            .build();

    assignment = Assignment
            .builder()
            .title(ASSIGNMENT_TITLE)
            .description(ASSIGNMENT_DESCRIPTION)
            .deadline(ASSIGNMENT_DEADLINE)
            .batch(batch)
            .file(file)
            .build();

    room = Room.builder().id(ROOM_ID).build();

    pageable = new PageRequest(0, 10);

    assignmentList = new ArrayList<>();
    assignmentList.add(assignment);

    assignmentPage = new PageImpl<>(assignmentList, pageable, 1);
    roomPage = new PageImpl<>(Collections.singletonList(room), pageable, 1);

    when(assignmentRepository.findByIdAndDeletedFalse(assignment.getId()))
            .thenReturn(Optional.of(assignment));
    when(assignmentRepository.findAllByBatchAndDeletedFalse(batch, pageable))
            .thenReturn(assignmentPage);
    when(assignmentRepository.save(assignment))
            .thenReturn(assignment);
    when(batchService.getBatchByCode(BATCH_CODE)).thenReturn(batch);
    when(resourceService.getFile(FILE_ID)).thenReturn(file);
    when(resourceService.markFilesUsed(Collections.singletonList(FILE_ID), true)).thenReturn(true);
    when(resourceService.markFilesUsed(Collections.singletonList(FILE_ID), false)).thenReturn(true);
    when(roomService.createRoomsByAssignment(assignment)).thenReturn(assignment);
    when(roomService.findById(ROOM_ID)).thenReturn(room);
    when(roomService.findAllRoomsByAssignmentId(any(String.class), eq(pageable))).thenReturn(roomPage);
    when(roomService.giveScoreToRoomByRoomId(ROOM_ID, 100)).thenReturn(room);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(assignmentRepository, batchService, resourceService, roomService);
  }

  @Test
  public void testFindAllAssignmentWithPageable() {
    Page<Assignment> result = assignmentService.findAllByBatchCodeAndPageable(pageable, BATCH_CODE);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isEqualTo(1);
    assertThat(result.getContent()).isEqualTo(assignmentList);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(assignmentRepository).findAllByBatchAndDeletedFalse(batch, pageable);
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
    verify(roomService).deleteAllRoomsByAssignmentId(assignment.getId());
    verify(resourceService).markFilesUsed(Collections.singletonList(FILE_ID), false);
    assignment.setDeleted(true);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testCreateAssignmentSuccess() {
    Assignment actual = assignmentService.createAssignment(assignment);
    assertThat(actual.getFile()).isEqualTo(file);
    assertThat(actual).isEqualTo(assignment);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(Collections.singletonList(FILE_ID), true);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(roomService).createRoomsByAssignment(assignment);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testCreateAssignmentNullFile() {
    assignment.setFile(null);
    Assignment actual = assignmentService.createAssignment(assignment);
    assertThat(actual.getFile()).isEqualTo(null);
    assertThat(actual).isEqualTo(assignment);
    verify(batchService).getBatchByCode(BATCH_CODE);
    verify(roomService).createRoomsByAssignment(assignment);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  public void testUpdateAssignmentSuccess() {
    Assignment assignmentWithFile = assignment;
    assignmentWithFile.setFile(file);
    when(assignmentRepository.save(assignmentWithFile))
            .thenReturn(assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignment);
    assertThat(actual.getFile()).isEqualTo(file);
    assertThat(actual).isEqualTo(assignment);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(Collections.singletonList(FILE_ID), true);
    verify(assignmentRepository).save(assignment);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignmentWithFile.getId());
  }

  @Test
  public void testUpdateAssignmentDifferentBatchAndFileSuccess() {
    Assignment assignmentWithFile = new Assignment();
    FileV2 anotherFile = FileV2.builder().id("id").build();
    BeanUtils.copyProperties(assignment, assignmentWithFile);
    assignmentWithFile.setFile(anotherFile);
    assignmentWithFile.setBatch(Batch.builder().code("CODE").build());
    when(resourceService.getFile("id")).thenReturn(anotherFile);
    when(resourceService.markFilesUsed(Collections.singletonList("id"), true)).thenReturn(true);
    when(assignmentRepository.findByIdAndDeletedFalse(assignmentWithFile.getId())).thenReturn(Optional.of(assignment));
    when(assignmentRepository.save(assignmentWithFile))
            .thenReturn(assignmentWithFile);
    Assignment actual = assignmentService.updateAssignment(assignmentWithFile);
    assertThat(actual.getFile()).isEqualTo(anotherFile);
    assertThat(actual).isEqualTo(assignmentWithFile);
    verify(resourceService).getFile("id");
    verify(resourceService).markFilesUsed(Collections.singletonList("id"), true);
    verify(resourceService).markFilesUsed(Collections.singletonList(FILE_ID), false);
    verify(roomService).deleteAllRoomsByAssignmentId(assignmentWithFile.getId());
    verify(roomService).createRoomsByAssignment(assignmentWithFile);
    verify(assignmentRepository).save(assignmentWithFile);
    verify(assignmentRepository).findByIdAndDeletedFalse(assignmentWithFile.getId());
  }

  @Test
  public void findAllRoomByAssignmentId() {
    Page<Room> actual = assignmentService.findAllRoomsByAssignmentId("id", pageable);
    assertThat(actual.getTotalElements()).isEqualTo(1);
    assertThat(actual.getContent().get(0)).isEqualTo(room);
    verify(roomService).findAllRoomsByAssignmentId("id", pageable);
  }

  @Test
  public void findRoomByIdTest() {
    Room actual = assignmentService.findRoomById(ROOM_ID);
    assertThat(actual).isNotNull();
    assertThat(actual).isEqualTo(room);
    verify(roomService).findById(ROOM_ID);
  }

  @Test
  public void giveScoreToRoom() {
    Room actual = assignmentService.giveScoreToRoomByRoomId(ROOM_ID, 100);
    assertThat(actual).isEqualTo(room);
    verify(roomService).giveScoreToRoomByRoomId(ROOM_ID, 100);
  }

  @Test
  public void deleteRoomByIdTest() {
    assignmentService.deleteRoomById(ROOM_ID);
    verify(roomService).deleteRoomById(ROOM_ID);
  }

  @Test
  public void copyAssignmentTest() {
    when(assignmentRepository.findByIdAndDeletedFalse("id")).thenReturn(Optional.of(assignment));
    Batch anotherBatch = Batch.builder().code("CODE").build();
    when(batchService.getBatchByCode("CODE")).thenReturn(anotherBatch);
    Assignment anotherAssignment = Assignment.builder().build();
    BeanUtils.copyProperties(assignment, anotherAssignment, "id");
    anotherAssignment.setBatch(anotherBatch);
    when(assignmentRepository.save(any(Assignment.class))).thenReturn(anotherAssignment);
    when(roomService.createRoomsByAssignment(anotherAssignment)).thenReturn(anotherAssignment);
    Assignment actual = assignmentService.copyAssignment("id", "CODE");
    assertThat(actual.getBatch().getCode()).isEqualTo("CODE");
    assertThat(actual.getId()).isNotEqualTo("id");
    verify(resourceService).markFilesUsed(Collections.singletonList(FILE_ID), true);
    verify(resourceService).getFile(FILE_ID);
    verify(batchService, times(2)).getBatchByCode("CODE");
    verify(assignmentRepository).findByIdAndDeletedFalse("id");
    verify(assignmentRepository).save(any(Assignment.class));
    verify(roomService).createRoomsByAssignment(anotherAssignment);
  }
}