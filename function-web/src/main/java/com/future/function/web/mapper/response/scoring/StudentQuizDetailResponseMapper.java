package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.OptionWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuestionWebResponse;
import com.future.function.web.model.response.feature.scoring.StudentQuizDetailWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    private static List<OptionWebResponse> removeCorrectField(List<OptionWebResponse> options) {
        return options
                .stream()
                .map(option -> {
                    option.setCorrect(null);
                    return option;
                })
                .collect(Collectors.toList());
    }

    private static StudentQuestionWebResponse toStudentQuestionWebResponse(StudentQuestion studentQuestion) {
        return StudentQuestionWebResponse
                .builder()
                .text(studentQuestion.getQuestion().getText())
                .number(studentQuestion.getNumber())
                .options(StudentQuizDetailResponseMapper
                        .removeCorrectField(OptionResponseMapper
                                .toListOfOptionWebResponse(studentQuestion
                                        .getQuestion()
                                        .getOptions())))
                .build();
    }

}
