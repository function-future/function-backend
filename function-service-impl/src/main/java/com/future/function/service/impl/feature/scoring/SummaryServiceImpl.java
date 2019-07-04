package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.model.enums.scoring.ScoringType;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sun.net.www.protocol.http.AuthenticationHeader;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentQuizService studentQuizService;

    @Autowired
    private UserService userService;

    @Override
    public List<SummaryDTO> findAllPointSummaryByStudentId(String studentId, String userId) {
        return Optional.ofNullable(userId)
                .map(userService::getUser)
                .map(user -> checkEligibilityUser(user, studentId))
                .map(userService::getUser)
                .map(User::getId)
                .map(this::getAllSummaryFromAssignmentAndQuiz)
                .orElseGet(ArrayList::new);
    }

    private String checkEligibilityUser(User user, String studentId) {
        if(user.getRole().equals(Role.STUDENT) && !user.getId().equals(studentId)) {
            throw new ForbiddenException("User not allowed");
        }
        return studentId;
    }

    private List<SummaryDTO> getAllSummaryFromAssignmentAndQuiz(String userId) {
        List<SummaryDTO> resultList = new ArrayList<>();
        roomService.findAllByStudentId(userId).stream().map(this::mapRoomToSummaryDTO).forEach(resultList::add);
        studentQuizService.findAllQuizByStudentId(userId).stream().map(this::mapQuizToSummaryDTO).forEach(resultList::add);
        return resultList;
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
