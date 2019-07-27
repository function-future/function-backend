package com.future.function.repository.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingRoomRepository extends MongoRepository<LoggingRoom, String> {

    Page<LoggingRoom> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<LoggingRoom> findAllTitleContainingIgnoreCaseAndByDeletedFalseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    Page<LoggingRoom> findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(User member, Pageable pageable);

    Page<LoggingRoom> findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(String keyword, User member, Pageable pageable);

}
