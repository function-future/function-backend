package com.future.function.service.api.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoggingRoomService {

    Page<LoggingRoom> getAllLoggingRooms(Pageable pageable);

    Page<LoggingRoom> getLoggingRoomsWithKeyword(String keyword, Pageable pageable);

    Page<LoggingRoom> getLoggingRoomsByMember(String memberId, Pageable pageable);

    Page<LoggingRoom> getLoggingRoomsByMemberWithKeyword(String keyword, String memberId, Pageable pageable);

    LoggingRoom getLoggingRoom (String loggingRoomId);

    LoggingRoom createLoggingRoom (LoggingRoom loggingRoom);

    LoggingRoom updateLoggingRoom (LoggingRoom loggingRoom);

    void deleteLoggingRoom (String loggingRoomId);

}
