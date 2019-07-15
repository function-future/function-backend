package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.model.enums.scoring.ScoringType;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.api.feature.scoring.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryServiceImpl implements SummaryService {

    private RoomService roomService;

    private StudentQuizService studentQuizService;

    private UserService userService;

    @Autowired
    public SummaryServiceImpl(RoomService roomService, StudentQuizService studentQuizService, UserService userService) {
        this.roomService = roomService;
        this.studentQuizService = studentQuizService;
        this.userService = userService;
    }

    @Override
    public StudentSummaryVO findAllPointSummaryByStudentId(String studentId, String userId) {
        return Optional.ofNullable(userId)
                .map(userService::getUser)
                .map(user -> checkEligibilityUser(user, studentId))
                .map(userService::getUser)
                .map(this::getAllSummaryFromAssignmentAndQuiz)
            .map(this::mapToStudentSummaryDTO)
                .orElseThrow(() -> new NotFoundException("Failed at #findAllPointSummaryByStudentId #SummaryService"));
    }

    private String checkEligibilityUser(User user, String studentId) {
        if(user.getRole().equals(Role.STUDENT) && !user.getId().equals(studentId)) {
            throw new ForbiddenException("Failed at #checkEligibilityUser #SummaryService");
        }
        return studentId;
    }

    private StudentSummaryVO mapToStudentSummaryDTO(Pair<User, List<SummaryVO>> pair) {
        return StudentSummaryVO.builder()
            .studentName(pair.getFirst().getName())
            .batchCode(pair.getFirst().getBatch().getCode())
            .university(pair.getFirst().getUniversity())
                .avatar(pair.getFirst().getPictureV2().getFileUrl())
            .scores(pair.getSecond())
            .build();
    }

    private Pair<User, List<SummaryVO>> getAllSummaryFromAssignmentAndQuiz(User user) {
        List<SummaryVO> resultList = new ArrayList<>();
        roomService.findAllByStudentId(user.getId()).stream().map(this::mapRoomToSummaryDTO).forEach(resultList::add);
        studentQuizService.findAllQuizByStudentId(user.getId()).stream().map(this::mapQuizToSummaryDTO).forEach(resultList::add);
        return Pair.of(user, resultList);
    }


    private SummaryVO mapRoomToSummaryDTO(Room room) {
        return SummaryVO.builder()
                .title(room.getAssignment().getTitle())
                .point(room.getPoint())
                .type(ScoringType.ASSIGNMENT.getType())
                .build();
    }

    private SummaryVO mapQuizToSummaryDTO(StudentQuizDetail studentQuizDetail) {
        return SummaryVO.builder()
                .title(studentQuizDetail.getStudentQuiz().getQuiz().getTitle())
                .point(studentQuizDetail.getPoint())
                .type(ScoringType.QUIZ.getType())
                .build();
    }
}
