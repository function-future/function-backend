package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuestionWebResponse;
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
                .point(studentQuizDetail.getPoint())
                .build();
    }

    public static PagingResponse<StudentQuestionWebResponse> toStudentQuestionWebResponses(List<StudentQuestion> studentQuestions) {
        List<StudentQuestionWebResponse> responseList = studentQuestions
                .stream()
                .map(StudentQuizDetailResponseMapper::toStudentQuestionWebResponse)
                .collect(Collectors.toList());

        return ResponseHelper.toPagingResponse(HttpStatus.OK, responseList, null);
    }

    private static StudentQuestionWebResponse toStudentQuestionWebResponse(StudentQuestion studentQuestion) {
        return StudentQuestionWebResponse
                .builder()
                .questionText(studentQuestion.getQuestion().getText())
                .options(OptionResponseMapper.toListOfOptionWebResponse(studentQuestion.getQuestion().getOptions()))
                .build();
    }

    public static DataResponse<StudentQuizDetailWebResponse> toStudentQuizDetailWebResponse(HttpStatus httpStatus,
                                                                                            StudentQuizDetail studentQuizDetail) {
        return ResponseHelper.toDataResponse(httpStatus, buildStudentQuizDetailWebResponse(studentQuizDetail));
    }

}