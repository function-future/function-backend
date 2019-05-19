package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.QuestionWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizDetailWebResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class StudentQuizDetailResponseMapper {

    public static DataResponse<StudentQuizDetailWebResponse> toStudentQuizDetailWebResponse(
            StudentQuizDetail studentQuizDetail) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildStudentQuizDetailWebResponse(studentQuizDetail));
    }

    private static StudentQuizDetailWebResponse buildStudentQuizDetailWebResponse(StudentQuizDetail studentQuizDetail) {
        return StudentQuizDetailWebResponse
                .builder()
                .questions(buildListOfQuestionWebResponse(studentQuizDetail))
                .point(studentQuizDetail.getPoint())
                .build();
    }

    private static List<QuestionWebResponse> buildListOfQuestionWebResponse(StudentQuizDetail studentQuizDetail) {
        return studentQuizDetail.getQuestions()
                .stream()
                .map(QuestionResponseMapper::toQuestionWebResponse)
                .map(DataResponse::getData)
                .collect(Collectors.toList());
    }

    public static DataResponse<StudentQuizDetailWebResponse> toStudentQuizDetailWebResponse(HttpStatus httpStatus,
                                                                                            StudentQuizDetail studentQuizDetail) {
        return ResponseHelper.toDataResponse(httpStatus, buildStudentQuizDetailWebResponse(studentQuizDetail));
    }

}
