package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {

    Page<Room> findAllByAssignmentIdAndDeletedFalse(String assignmentId, Pageable page);

    Optional<Room> findByIdAndDeletedFalse(String id);
}
