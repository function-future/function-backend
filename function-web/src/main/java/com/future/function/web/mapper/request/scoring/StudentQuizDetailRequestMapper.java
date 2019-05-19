package com.future.function.web.mapper.request.scoring;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.web.model.request.scoring.StudentQuizDetailWebRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class StudentQuizDetailRequestMapper {

    private ObjectValidator objectValidator;

    private QuestionRequestMapper questionRequestMapper;

    @Autowired
    public StudentQuizDetailRequestMapper(ObjectValidator objectValidator,
                                          QuestionRequestMapper questionRequestMapper) {
        this.objectValidator = objectValidator;
        this.questionRequestMapper = questionRequestMapper;
    }

    public StudentQuizDetail toStudentQuizDetail(StudentQuizDetailWebRequest request) {
        return toValidatedStudentQuizDetail(request);
    }

    private StudentQuizDetail toValidatedStudentQuizDetail(StudentQuizDetailWebRequest request) {
        request = objectValidator.validate(request);

        List<Question> questionList = request.getQuestions()
                .stream()
                .map(questionRequestMapper::toStudentQuizQuestion)
                .collect(Collectors.toList());
        return StudentQuizDetail
                .builder()
                .questions(questionList)
                .build();
    }
}
