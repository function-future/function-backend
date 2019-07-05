package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.dto.scoring.StudentSummaryDTO;
import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.model.enums.scoring.ScoringType;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.api.feature.scoring.SummaryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentQuizService studentQuizService;

    @Autowired
    private UserService userService;

    @Override
    public StudentSummaryDTO findAllPointSummaryByStudentId(String studentId, String userId) {
        return Optional.ofNullable(userId)
                .map(userService::getUser)
                .map(user -> checkEligibilityUser(user, studentId))
                .map(userService::getUser)
                .map(this::getAllSummaryFromAssignmentAndQuiz)
            .map(this::mapToStudentSummaryDTO)
            .orElseThrow(() -> new NotFoundException("Student not found at #findAllPointSummaryByStudentId"));
    }

    private String checkEligibilityUser(User user, String studentId) {
        if(user.getRole().equals(Role.STUDENT) && !user.getId().equals(studentId)) {
            throw new ForbiddenException("User not allowed");
        }
        return studentId;
    }

    private StudentSummaryDTO mapToStudentSummaryDTO(Pair<User, List<SummaryDTO>> pair) {
        return StudentSummaryDTO.builder()
            .studentName(pair.getFirst().getName())
            .batchCode(pair.getFirst().getBatch().getCode())
            .university(pair.getFirst().getUniversity())
            .scores(pair.getSecond())
            .build();
    }

    private Pair<User, List<SummaryDTO>> getAllSummaryFromAssignmentAndQuiz(User user) {
        List<SummaryDTO> resultList = new ArrayList<>();
        roomService.findAllByStudentId(user.getId()).stream().map(this::mapRoomToSummaryDTO).forEach(resultList::add);
        studentQuizService.findAllQuizByStudentId(user.getId()).stream().map(this::mapQuizToSummaryDTO).forEach(resultList::add);
        return Pair.of(user, resultList);
    }


    private SummaryDTO mapRoomToSummaryDTO(Room room) {
        return SummaryDTO.builder()
                .title(room.getAssignment().getTitle())
                .point(room.getPoint())
                .type(ScoringType.ASSIGNMENT.getType())
                .build();
    }

    private SummaryDTO mapQuizToSummaryDTO(StudentQuizDetail studentQuizDetail) {
        return SummaryDTO.builder()
                .title(studentQuizDetail.getStudentQuiz().getQuiz().getTitle())
                .point(studentQuizDetail.getPoint())
                .type(ScoringType.QUIZ.getType())
                .build();
    }
}
