package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface to act as Assignment Service API
 */
public interface AssignmentService {

  Page<Assignment> findAllByBatchCodeAndPageable(String batchCode, Pageable pageable);

  Assignment findById(String id);

  Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable);

    Page<Room> findAllRoomsByStudentId(String studentId, Pageable pageable, String userId);

  Room findRoomById(String id, String studentId);

  Assignment copyAssignment(String assignmentId, String targetBatchCode);

  Assignment createAssignment(Assignment request);

  Assignment updateAssignment(Assignment request);

  Room giveScoreToRoomByRoomId(String roomId, String userId, Integer point);

  void deleteRoomById(String id);

  void deleteById(String id);

}
