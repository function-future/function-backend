package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizDetailRepository;
import com.future.function.service.api.feature.scoring.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentQuizDetailServiceImpl implements StudentQuizDetailService {

    private StudentQuizDetailRepository studentQuizDetailRepository;

    private StudentQuizService studentQuizService;

    private QuestionService questionService;

    private OptionService optionService;

    private StudentQuestionService studentQuestionService;

    @Autowired
    public StudentQuizDetailServiceImpl(StudentQuizDetailRepository studentQuizDetailRepository,
                                        StudentQuizService studentQuizService,
                                        QuestionService questionService,
                                        OptionService optionService,
                                        StudentQuestionService studentQuestionService) {
        this.studentQuizDetailRepository = studentQuizDetailRepository;
        this.studentQuizService = studentQuizService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.studentQuestionService = studentQuestionService;
    }

    @Override
    public StudentQuizDetail findLatestByStudentQuizId(String studentQuizId) {
        return Optional.ofNullable(studentQuizId)
                .map(studentQuizService::findById)
                .map(StudentQuiz::getId)
                .flatMap(studentQuizDetailRepository::findFirstByStudentQuizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
    }

    @Override
    public List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId) {
        return Optional.ofNullable(studentQuizId)
                .map(this::findLatestByStudentQuizId)
                .map(StudentQuizDetail::getId)
                .map(studentQuestionService::findAllByStudentQuizDetailId)
                .orElseThrow(() -> new UnsupportedOperationException("Student Quiz not found"));
    }

    @Override
    public StudentQuizDetail answerStudentQuiz(String studentQuizId, List<StudentQuestion> answers) {
        StudentQuizDetail detail = Optional.ofNullable(studentQuizId)
                .map(this::findLatestByStudentQuizId)
                .orElseThrow(() -> new NotFoundException("Student quiz detail not found"));

        answers = answers.stream()
                .peek(answer -> answer.setStudentQuizDetail(detail))
                .collect(Collectors.toList());

        Integer point = studentQuestionService.postAnswerForAllStudentQuestion(answers);
        detail.setPoint(point);
        return detail;
    }

    @Override
    public StudentQuizDetail createStudentQuizDetail(StudentQuiz studentQuiz, List<StudentQuestion> questions) {
        return Optional.ofNullable(studentQuiz)
                .map(this::toStudentQuizDetail)
                .map(detail -> {
                    detail = studentQuizDetailRepository.save(detail);
                    validateQuestionsAndCreateStudentQuestions(detail, questions);
                    return detail;
                })
                .orElseThrow(() -> new UnsupportedOperationException("Answer quiz failed"));
    }

    @Override
    public List<StudentQuestion> validateQuestionsAndCreateStudentQuestions(StudentQuizDetail studentQuizDetail,
                                                                            List<StudentQuestion> questions) {
        return Optional.ofNullable(questions)
                .filter(questionList -> questionList.size() > 0)
                .map(questionList -> studentQuestionService.createStudentQuestionsByStudentQuizDetail(studentQuizDetail, questionList))
                .orElseGet(ArrayList::new);
    }

    @Override
    public void deleteByStudentQuiz(StudentQuiz studentQuiz) {
        Optional.ofNullable(studentQuiz)
                .map(StudentQuiz::getId)
                .map(this::findLatestByStudentQuizId)
                .ifPresent(detail -> {
                    detail.setDeleted(true);
                    studentQuestionService.deleteAllByStudentQuizDetailId(detail.getId());
                    studentQuizDetailRepository.save(detail);
                });
    }

    private StudentQuizDetail toStudentQuizDetail(StudentQuiz studentQuiz) {

        return StudentQuizDetail
                .builder()
                .studentQuiz(studentQuiz)
                .point(0)
                .build();
    }
}
