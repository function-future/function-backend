package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.repository.feature.scoring.RoomRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.CommentService;
import com.future.function.service.api.feature.scoring.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Override
    public Page<Room> findAllRoomsByAssignmentId(String assignmentId, Pageable pageable) {
        return Optional.ofNullable(assignmentId)
                .map(id -> roomRepository.findAllByAssignmentIdAndDeletedFalse(assignmentId, pageable))
                .orElseGet(() -> new PageImpl<>(new ArrayList<>(), pageable, 0));
    }

    @Override
    public Room findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(roomRepository::findByIdAndDeletedFalse)
                .orElseThrow(() -> new NotFoundException("Room not found"));
    }

    @Override
    public List<Comment> findAllCommentsByRoomId(String roomId) {
        return commentService.findAllCommentsByRoomId(roomId);
    }

    @Override
    public Comment createComment(Comment comment) {
        Room room = this.findById(comment.getRoom().getId());
        User author = userService.getUser(comment.getAuthor().getId());
        comment.setRoom(room);
        comment.setAuthor(author);
        return commentService.createCommentByRoom(room, comment);
    }

    @Override
    public Assignment createRoomsByAssignment(Assignment assignment) {
        List<User> userListFromBatch = userService.getStudentsByBatchCode(assignment.getBatch().getCode());
        userListFromBatch
                .forEach(user -> this.createRoomForUserAndSave(user, assignment));
        return assignment;
    }

    private void createRoomForUserAndSave(User user, Assignment assignment) {
        Room room = Room
                .builder()
                .assignment(assignment)
                .point(0)
                .student(user)
                .build();
        roomRepository.save(room);
    }

    @Override
    public Room giveScoreToRoomByRoomId(String roomId, Integer point) {
        Room room = this.findById(roomId);
        room.setPoint(point);
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoomById(String id) {
        Optional.ofNullable(id)
                .flatMap(roomRepository::findByIdAndDeletedFalse)
                .ifPresent(this::setDeleteAsTrueAndSave);
    }

    @Override
    public void deleteAllRoomsByAssignmentId(String assignmentId) {
        Optional.ofNullable(assignmentId)
                .map(roomRepository::findAllByAssignmentIdAndDeletedFalse)
                .ifPresent(list -> list.forEach(this::setDeleteAsTrueAndSave));
    }

    private void setDeleteAsTrueAndSave(Room room) {
        room.setDeleted(true);
        roomRepository.save(room);
    }
}
