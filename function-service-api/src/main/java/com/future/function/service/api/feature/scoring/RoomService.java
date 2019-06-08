package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {

    Page<Room> findAllRoomsByAssignmentId(String assignmentId);

    List<Room> createRoomsByAssignment(Assignment assignment);

    void deleteRoomById(String id);

    void deleteAllRoomsByAssignmentId(String assignmentId);

}
