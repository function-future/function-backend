package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {

  private AssignmentRepository assignmentRepository;
  private RoomService roomService;
  private ResourceService resourceService;
  private BatchService batchService;

  @Autowired
  public AssignmentServiceImpl(AssignmentRepository assignmentRepository, RoomService roomService,
                               ResourceService resourceService, BatchService batchService) {
    this.assignmentRepository = assignmentRepository;
    this.roomService = roomService;
    this.resourceService = resourceService;
    this.batchService = batchService;
  }

  @Override
  public Page<Assignment> findAllByBatchCodeAndPageable(String batchCode, Pageable pageable) {
    return Optional.ofNullable(batchCode)
            .map(batchService::getBatchByCode)
            .map(batch -> assignmentRepository.findAllByBatchAndDeletedFalse(batch, pageable))
            .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public Assignment findById(String id) {
    return Optional.ofNullable(id)
            .flatMap(assignmentRepository::findByIdAndDeletedFalse)
            .orElseThrow(() -> new NotFoundException("#Failed at #findById #AssignmentService"));
  }

  @Override
  public Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable) {
    return roomService.findAllRoomsByAssignmentId(assignmentId, pageable);
  }

  @Override
  public Page<Room> findAllRoomsByStudentId(String studentId, Pageable pageable, String userId) {
      return roomService.findAllByStudentId(studentId, pageable, userId);
  }

  @Override
  public Room findRoomById(String id, String userId) {
    return roomService.findById(id, userId);
  }

  @Override
  public Assignment copyAssignment(String assignmentId, String targetBatchCode) {
    Batch targetBatch = batchService.getBatchByCode(targetBatchCode);
    return Optional.ofNullable(assignmentId)
            .map(this::findById)
            .map(assignment -> initializeNewAssignment(targetBatch, assignment))
            .map(this::createAssignment)
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #copyAssignment #AssignmentService"));
  }

  private Assignment initializeNewAssignment(Batch targetBatch, Assignment assignment) {
    Assignment newAssignment = Assignment.builder().build();
    CopyHelper.copyProperties(assignment, newAssignment);
    newAssignment.setBatch(targetBatch);
    return newAssignment;
  }

  @Override
  public Assignment createAssignment(Assignment assignment) {
    return Optional.ofNullable(assignment)
            .map(this::storeAssignmentFile)
            .map(this::findAndSetAssignmentBatch)
            .map(assignmentRepository::save)
            .map(roomService::createRoomsByAssignment)
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #createAssignment #AssignmentService"));
  }

  private Assignment findAndSetAssignmentBatch(Assignment value) {
    Batch batch = batchService.getBatchByCode(value.getBatch().getCode());
    value.setBatch(batch);
    return value;
  }

  private Assignment storeAssignmentFile(Assignment assignment) {
    return Optional.ofNullable(assignment)
            .map(Assignment::getFile)
            .map(FileV2::getId)
            .filter(fileId -> resourceService.markFilesUsed(Collections.singletonList(fileId), true))
            .map(resourceService::getFile)
            .map(file -> {
              assignment.setFile(file);
              return assignment;
            })
            .orElse(assignment);
  }

  @Override
  public Assignment updateAssignment(Assignment request) {
    return Optional.ofNullable(request)
            .map(Assignment::getId)
            .map(this::findById)
            .map(foundAssignment -> setAssignmentFile(request, foundAssignment))
            .map(foundAssignment -> copyAssignmentRequestAttributesBasedOnFileExistence(request, foundAssignment))
            .map(this::storeAssignmentFile)
            .map(assignmentRepository::save)
            .orElse(request);
  }

  private Assignment copyAssignmentRequestAttributesBasedOnFileExistence(Assignment request, Assignment foundAssignment) {
    if (Objects.isNull(request.getFile())) {
      CopyHelper.copyProperties(request, foundAssignment, FieldName.Assignment.FILE, FieldName.Assignment.BATCH);
    } else {
      CopyHelper.copyProperties(request, foundAssignment, FieldName.Assignment.BATCH);
    }
    return foundAssignment;
  }

    private Assignment setAssignmentFile(Assignment request, Assignment foundAssignment) {
    return Optional.ofNullable(request)
        .map(requestedAssignment -> checkAndMarkFileAsNotUsedIfFileInDBExist(requestedAssignment, foundAssignment))
        .orElse(foundAssignment);
  }

  private Assignment checkAndMarkFileAsNotUsedIfFileInDBExist(Assignment request, Assignment foundAssignment) {
    if(checkIfRequestedFileNull(request, foundAssignment) || checkIfFileIdEquals(request, foundAssignment)) {
      resourceService.markFilesUsed(Collections.singletonList(foundAssignment.getFile().getId()), false);
      foundAssignment.setFile(null);
    }
    return foundAssignment;
  }

  private boolean checkIfRequestedFileNull(Assignment request, Assignment foundAssignment) {
    return Objects.nonNull(foundAssignment.getFile()) && Objects.isNull(request.getFile());
  }

  private boolean checkIfFileIdEquals(Assignment request, Assignment foundAssignment) {
    return Objects.nonNull(foundAssignment.getFile()) && !foundAssignment.getFile().getId().equals(request.getFile().getId());
  }

  @Override
  public Room giveScoreToRoomByRoomId(String roomId, String userId, Integer point) {
    return roomService.giveScoreToRoomByRoomId(roomId, userId, point);
  }

  @Override
  public void deleteRoomById(String id) {
    roomService.deleteRoomById(id);
  }

  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
            .map(this::findById)
            .ifPresent(assignment -> {
              roomService.deleteAllRoomsByAssignmentId(assignment.getId());
              markAssignmentFileAsNotUsed(assignment);
              assignment.setDeleted(true);
              assignmentRepository.save(assignment);
            });
  }

  private void markAssignmentFileAsNotUsed(Assignment assignment) {
    if (Objects.nonNull(assignment.getFile()) && !StringUtils.isEmpty(assignment.getFile().getId())) {
      resourceService.markFilesUsed(Collections.singletonList(assignment.getFile().getId()), false);
    }
  }
}
