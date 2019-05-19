package com.future.function.web.mapper.request.scoring;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.web.model.request.scoring.StudentQuizWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentQuizRequestMapper {

    private ObjectValidator objectValidator;

    @Autowired
    public StudentQuizRequestMapper(ObjectValidator objectValidator) {
        this.objectValidator = objectValidator;
    }

    public StudentQuiz toStudentQuiz(StudentQuizWebRequest request) {
        return toValidatedStudentQuiz(request);
    }

    private StudentQuiz toValidatedStudentQuiz(StudentQuizWebRequest request) {
        request = objectValidator.validate(request);
        return StudentQuiz
                .builder()
                .quiz(Quiz.builder().id(request.getQuizId()).build())
                .build();
    }

}
