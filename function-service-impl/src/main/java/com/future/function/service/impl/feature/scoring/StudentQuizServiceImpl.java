package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class StudentQuizServiceImpl implements StudentQuizService {

    private StudentQuizRepository studentQuizRepository;

    private UserService userService;

    private QuizService quizService;

    private BatchService batchService;

    @Autowired
    public StudentQuizServiceImpl(StudentQuizRepository studentQuizRepository, UserService userService, QuizService quizService) {
        this.studentQuizRepository = studentQuizRepository;
        this.userService = userService;
        this.quizService = quizService;
    }

    @Override
    public Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable) {
        return studentQuizRepository.findAllByStudentId(studentId, pageable);
    }

    @Override
    public StudentQuiz findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
    }

    @Override
    public StudentQuiz createStudentQuiz(String userId, Quiz quiz) {
        return Optional.ofNullable(quiz)
                .map(Quiz::getId)
                .map(this::getQuizIfExist)
                .filter(Objects::nonNull)
                .map(quizObj -> toStudentQuizWithUserAndQuiz(userId, quizObj))
                .orElseThrow(() -> new UnsupportedOperationException("Create quiz failed"));
    }

    @Override
    public StudentQuiz createStudentQuizByBatchCode(Integer batchCode, Quiz quiz) {
//        return Optional.ofNullable(batchCode)
//                .map(batchService::getBatchByCode)
////                .map(userService::);
//                .map()
        return null;
    }

    @Override
    public StudentQuiz copyQuizFromBatch(Integer originBatch, Integer targetBatch, String studentQuizId) {
//        List<String> studentIds = userService.
        return Optional.ofNullable(studentQuizId)
                .map(this::findById)
                //change this logic when core feature find student by batch is done
                .map(studentquiz -> studentquiz)
                .map(studentQuizRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Copy Quiz Failed"));
    }

    private Quiz getQuizIfExist(String quizId) {
        return Optional.of(quizId)
                .flatMap(studentQuizRepository::findByQuizId)
                .filter(Objects::isNull)
                .map(quiz -> quizService.findById(quizId))
                .orElse(null);
    }

    private StudentQuiz toStudentQuizWithUserAndQuiz(String userId, Quiz quiz) {
        return StudentQuiz
                .builder()
                .quiz(quizService.findById(quiz.getId()))
                .student(userService.getUser(userId))
                .trials(quiz.getTrials())
                .done(false)
                .build();
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(id)
                .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
                .ifPresent(quiz -> {
                    quiz.setDeleted(true);
                    studentQuizRepository.save(quiz);
                });
    }
}
