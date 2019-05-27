package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentQuizServiceImpl implements StudentQuizService {

    @Autowired
    private StudentQuizRepository studentQuizRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private StudentQuizDetailService studentQuizDetailService;

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
    public StudentQuiz createStudentQuizAndSave(String userId, Quiz quiz) {
        return Optional.ofNullable(quiz)
                .filter(Objects::nonNull)
                .map(quizObj -> toStudentQuizWithUserAndQuiz(userId, quizObj))
                .map(studentQuizRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Create quiz failed"));
    }

    @Override
    public Quiz createStudentQuizByBatchCode(String batchCode, Quiz quiz) {
        return Optional.ofNullable(batchCode)
                .map(userService::getStudentsByBatchCode)
                .map(userList -> createStudentQuizFromUserList(quiz, userList))
                .map(studentQuiz -> studentQuiz.get(0))
                .map(this::createStudentQuizDetailAndSave)
                .map(returnValue -> quiz)
                .map(returnValue -> {
                    returnValue.setBatch(batchService.getBatchByCode(batchCode));
                    return returnValue;
                })
                .orElseThrow(() -> new UnsupportedOperationException("Batch code is null"));
    }

    private StudentQuizDetail createStudentQuizDetailAndSave(StudentQuiz studentQuiz) {
        StudentQuizDetail detail = StudentQuizDetail
                .builder()
                .studentQuiz(studentQuiz)
                .point(0)
                .build();
        return studentQuizDetailService.createStudentQuizDetail(studentQuiz, null);
    }

    @Override
    public Quiz copyQuizFromBatch(String targetBatch, Quiz quiz) {
        return Optional.ofNullable(quiz)
                .map(value -> createStudentQuizzesAndReturnNewQuiz(targetBatch, value))
                .map(quizService::createQuiz)
                .orElseThrow(() -> new UnsupportedOperationException("Copy Quiz Failed"));
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

    @Override
    public void deleteByBatchCodeAndQuiz(String batchCode, String quizId) {
        List<User> userList = userService.getStudentsByBatchCode(batchCode);
        userList.stream()
                .map(User::getId)
                .map(id -> studentQuizRepository.findByStudentIdAndQuizId(id, quizId))
                .forEach(studentQuizOptional -> studentQuizOptional
                        .ifPresent(studentQuiz -> {
                            studentQuiz.setDeleted(true);
                            studentQuizRepository.save(studentQuiz);
                            studentQuizDetailService.deleteByStudentQuiz(studentQuiz);
                        }));
    }

    private List<StudentQuiz> createStudentQuizFromUserList(Quiz quiz, List<User> userList) {
        return userList.stream()
                .map(User::getId)
                .map(userId -> createStudentQuizAndSave(userId, quiz))
                .collect(Collectors.toList());
    }

    private Quiz createStudentQuizzesAndReturnNewQuiz(String targetBatch, Quiz quiz) {
        quiz = this.createStudentQuizByBatchCode(targetBatch, quiz);
        Quiz newQuiz = new Quiz();
        BeanUtils.copyProperties(quiz, newQuiz, "id");
        return newQuiz;
    }

    private StudentQuiz toStudentQuizWithUserAndQuiz(String userId, Quiz quiz) {
        return StudentQuiz
                .builder()
                .quiz(quiz)
                .student(userService.getUser(userId))
                .trials(quiz.getTrials())
                .done(false)
                .build();
    }

}
