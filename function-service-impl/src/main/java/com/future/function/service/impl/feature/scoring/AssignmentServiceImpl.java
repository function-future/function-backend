package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.impl.helper.CopyHelper;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class used to manipulate Assignment Entity
 * Used AssignmentRepository and FileService to support manipulation of Assignment Entity
 */
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

  /**
   * Used to find All Assignment Object from Repository With Paging, Filtering, And Search Keyword
   *
   * @param pageable (Pageable Object)
   * @return Page<Assignment>
   */
  @Override
  public Page<Assignment> findAllByBatchCodeAndPageable(String batchCode, Pageable pageable) {
    Batch batch = batchService.getBatchByCode(batchCode);
    return assignmentRepository.findAllByBatchAndDeletedFalse(batch, pageable);
  }

  /**
   * Used to Find Assignment Object From Repository With Passed Id and Not Deleted
   *
   * @param id (String)
   * @return Assignment Object
   * @throws NotFoundException if Assignment Object is not found or the id is null
   */
  @Override
  public Assignment findById(String id) {
    return Optional.ofNullable(id)
            .map(assignmentRepository::findByIdAndDeletedFalse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .orElseThrow(() -> new NotFoundException("Assignment Not Found"));
  }

  @Override
  public Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable) {
    return roomService.findAllRoomsByAssignmentId(assignmentId, pageable);
  }

  @Override
  public Room findRoomById(String id, String userId) {
    return roomService.findById(id, userId);
  }

  @Override
  public Assignment copyAssignment(String assignmentId, String targetBatchCode) {
    Assignment assignment = this.findById(assignmentId);
    Assignment newAssignment = Assignment.builder().build();
    Batch targetBatchObj = batchService.getBatchByCode(targetBatchCode);
    CopyHelper.copyProperties(assignment, newAssignment);
    newAssignment.setBatch(targetBatchObj);
    return this.createAssignment(newAssignment);
  }

  /**
   * Used to create new Assignment Data in Repository With / Without file
   *
   * @param assignment (Assignment Object)
   * @return Saved Assignment Object
   */
  @Override
  public Assignment createAssignment(Assignment assignment) {
    assignment = storeAssignmentFile(assignment);
    Batch batch = batchService.getBatchByCode(assignment.getBatch().getCode());
    assignment.setBatch(batch);
    assignment = assignmentRepository.save(assignment);
    return roomService.createRoomsByAssignment(assignment);
  }

  /**
   * Used to store Multipart File by using FileService which sent with Assignment object
   *
   * @param assignment (Assignment Object)
   * @return Assignment with / without the saved file
   */
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

  /**
   * Used to update existed Assignment in Repository with Assignment Object request and / no Multipart File
   *
   * @param request (Assignment Object)
   * @return Saved Assignment Object
   */
  @Override
  public Assignment updateAssignment(Assignment request) {
    Assignment oldAssignment = this.findById(request.getId());
    boolean isBatchChanged = isBatchChanged(request, oldAssignment);
    if (isFilesChanged(request, oldAssignment)) {
      resourceService.markFilesUsed(Collections.singletonList(oldAssignment.getFile().getId()), false);
    }
    CopyHelper.copyProperties(request, oldAssignment);
    oldAssignment = storeAssignmentFile(oldAssignment);
    oldAssignment = assignmentRepository.save(oldAssignment);
    if (isBatchChanged) {
      roomService.deleteAllRoomsByAssignmentId(oldAssignment.getId());
      roomService.createRoomsByAssignment(oldAssignment);
    }
    return oldAssignment;
  }

  private boolean isBatchChanged(Assignment request, Assignment oldAssignment) {
    return !request.getBatch().getCode().equals(oldAssignment.getBatch().getCode());
  }

  private boolean isFilesChanged(Assignment request, Assignment oldAssignment) {
    return !request.getFile().getId().equals(oldAssignment.getFile().getId());
  }

  @Override
  public Room giveScoreToRoomByRoomId(String roomId, String userId, Integer point) {
    return roomService.giveScoreToRoomByRoomId(roomId, userId, point);
  }

  @Override
  public void deleteRoomById(String id) {
    roomService.deleteRoomById(id);
  }

  /**
   * Used to delete existing Assignment From Repository With passed id
   *
   * @param id (String)
   */
  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
            .map(this::findById)
            .ifPresent(assignment -> {
              roomService.deleteAllRoomsByAssignmentId(assignment.getId());
              if (Objects.nonNull(assignment.getFile()) && !StringUtils.isEmpty(assignment.getFile().getId())) {
                resourceService.markFilesUsed(Collections.singletonList(assignment.getFile().getId()), false);
              }
              assignment.setDeleted(true);
              assignmentRepository.save(assignment);
            });
  }
}
