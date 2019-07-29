package com.future.function.service.impl.feature.communication.logging;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.logging.LoggingRoomRepository;
import com.future.function.service.api.feature.communication.logging.LoggingRoomService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoggingRoomServiceImpl implements LoggingRoomService {

    private final LoggingRoomRepository loggingRoomRepository;

    private final UserService userService;

    @Autowired
    public LoggingRoomServiceImpl(LoggingRoomRepository loggingRoomRepository, UserService userService) {
        this.loggingRoomRepository = loggingRoomRepository;
        this.userService = userService;
    }

    @Override
    public Page<LoggingRoom> getLoggingRoomsByMember(String memberId, Pageable pageable) {
        if(userService.getUser(memberId).getRole().equals(Role.STUDENT)) {
            return loggingRoomRepository.findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(
                    userService.getUser(memberId),
                    pageable
            );
        }
        return loggingRoomRepository.findAllByDeletedFalseOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<LoggingRoom> getLoggingRoomsByMemberWithKeyword(String keyword, String memberId, Pageable pageable) {
        if(userService.getUser(memberId).getRole().equals(Role.STUDENT)) {
            return loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(
                    keyword,
                    userService.getUser(memberId),
                    pageable
            );
        }
        return loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(keyword, pageable);
    }

    @Override
    public LoggingRoom getLoggingRoom(String loggingRoomId) {
        return Optional.of(loggingRoomId)
                .map(loggingRoomRepository::findOne)
                .orElseThrow(() -> new NotFoundException("Logging Room not Found"));
    }

    @Override
    public LoggingRoom createLoggingRoom(LoggingRoom loggingRoom) {
        return Optional.of(loggingRoom)
                .map(this::setMembers)
                .map(loggingRoomRepository::save)
                .orElseThrow(UnsupportedOperationException::new);
    }

    @Override
    public LoggingRoom updateLoggingRoom(LoggingRoom loggingRoom) {
        return Optional.of(loggingRoom)
                .map(LoggingRoom::getId)
                .map(loggingRoomRepository::findOne)
                .map(savedRoom -> this.updateMember(savedRoom, loggingRoom))
                .map(savedRoom -> this.copyProperties(savedRoom, loggingRoom))
                .map(loggingRoomRepository::save)
                .orElse(loggingRoom);
    }

    @Override
    public void deleteLoggingRoom(String loggingRoomId) {
        Optional.ofNullable(loggingRoomId)
                .map(loggingRoomRepository::findOne)
                .ifPresent(this::softDeletedHelper);
    }

    private void softDeletedHelper(LoggingRoom loggingRoom) {
        loggingRoom.setDeleted(true);
        loggingRoomRepository.save(loggingRoom);
    }

    private LoggingRoom copyProperties(LoggingRoom savedRoom, LoggingRoom loggingRoom) {
        savedRoom.setTitle(loggingRoom.getTitle());
        savedRoom.setDescription(loggingRoom.getDescription());
        return savedRoom;
    }

    private LoggingRoom setMembers(LoggingRoom loggingRoom) {
        List<User> members = new ArrayList<>();
        if (loggingRoom.getMembers() != null) {
            loggingRoom.getMembers().forEach(member -> {
                members.add(userService.getUser(member.getId()));
            });
        }
        loggingRoom.setMembers(members);
        return loggingRoom;
    }

    private LoggingRoom updateMember(LoggingRoom savedLoggingRoom, LoggingRoom newLoggingRoom) {
        savedLoggingRoom.setMembers(newLoggingRoom.getMembers());
        return this.setMembers(savedLoggingRoom);
    }

}
