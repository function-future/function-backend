package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizDetailRepository;
import com.future.function.service.api.feature.scoring.OptionService;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentQuizDetailServiceImpl implements StudentQuizDetailService {

    private StudentQuizDetailRepository studentQuizDetailRepository;

    private StudentQuizService studentQuizService;

    private QuestionService questionService;

    private OptionService optionService;

    @Autowired
    public StudentQuizDetailServiceImpl(StudentQuizDetailRepository studentQuizDetailRepository,
                                        StudentQuizService studentQuizService,
                                        QuestionService questionService,
                                        OptionService optionService) {
        this.studentQuizDetailRepository = studentQuizDetailRepository;
        this.studentQuizService = studentQuizService;
        this.questionService = questionService;
        this.optionService = optionService;
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
    public StudentQuizDetail answerStudentQuiz(String studentQuizId, List<Question> questionList) {
        return Optional.ofNullable(studentQuizId)
                .map(studentQuizService::findById)
                .map(studentQuiz -> toStudentQuizDetail(studentQuiz, questionList))
                .map(studentQuizDetailRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Answer quiz failed"));
    }

    private StudentQuizDetail toStudentQuizDetail(StudentQuiz studentQuiz, List<Question> questionList) {

        long totalRightAnswer = questionList
                .stream()
                .filter(this::getQuestion)
                .filter(this::checkOptionCorrect)
                .count();

        return StudentQuizDetail
                .builder()
                .studentQuiz(studentQuiz)
                .questions(questionList)
                .point(getTotalPoint(questionList, totalRightAnswer))
                .build();
    }

    private int getTotalPoint(List<Question> questionList, long totalRightAnswer) {
        return Integer.parseInt(String.valueOf(totalRightAnswer)) / questionList.size() * 100;
    }

    private boolean getQuestion(Question question) {
        return Optional.of(question)
                .map(Question::getId)
                .map(questionService::findById)
                .map(Objects::nonNull)
                .orElse(false);
    }

    private boolean checkOptionCorrect(Question question) {
        return Optional.of(question)
                .map(Question::getOptions)
                .map(this::getCorrectAnswer)
                .map(optionService::isOptionCorrect)
                .orElse(false);

    }

    private Option getCorrectAnswer(List<Option> options) {
        return options
                .stream()
                .filter(Option::isCorrect)
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("No Correct Answer"));
    }
}
