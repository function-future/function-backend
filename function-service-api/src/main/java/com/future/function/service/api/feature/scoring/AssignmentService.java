package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface to act as Assignment Service API
 */
public interface AssignmentService {

  Page<Assignment> findAllByBatchCodeAndPageable(Pageable pageable, String batchCode);

  Assignment findById(String id);

  Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable);

  Room findRoomById(String id);

  Assignment copyAssignment(String assignmentId, String targetBatchCode);

  Assignment createAssignment(Assignment request);

  Assignment updateAssignment(Assignment request);

  Room giveScoreToRoomByRoomId(String roomId, Integer point);

  void deleteRoomById(String id);

  void deleteById(String id);

}
