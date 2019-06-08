package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.UserService;
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

    private StudentQuizRepository studentQuizRepository;
    private UserService userService;
    private StudentQuizDetailService studentQuizDetailService;

    @Autowired
    public StudentQuizServiceImpl(StudentQuizRepository studentQuizRepository, UserService userService,
                                  StudentQuizDetailService studentQuizDetailService) {
        this.studentQuizRepository = studentQuizRepository;
        this.userService = userService;
        this.studentQuizDetailService = studentQuizDetailService;
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
    public List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId) {
        return studentQuizDetailService.findAllQuestionsByStudentQuizId(studentQuizId);
    }

    @Override
    public List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(String studentQuizId) {
        StudentQuiz studentQuiz = findAndUpdateTrials(studentQuizId);
        return studentQuizDetailService.findAllUnansweredQuestionsByStudentQuizId(studentQuiz.getId());
    }

    private StudentQuiz findAndUpdateTrials(String studentQuizId) {
        StudentQuiz studentQuiz = this.findById(studentQuizId);
        studentQuiz.setTrials(studentQuiz.getTrials() - 1);
        return studentQuizRepository.save(studentQuiz);
    }

    @Override
    public StudentQuizDetail answerQuestionsByStudentQuizId(String studentQuizId, List<StudentQuestion> answers) {
        return studentQuizDetailService.answerStudentQuiz(studentQuizId, answers);
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
                .filter(userList -> !userList.isEmpty())
                .map(userList -> createStudentQuizFromUserList(quiz, userList))
                .map(studentQuiz -> studentQuiz.get(0))
                .map(this::createStudentQuizDetailAndSave)
                .map(returnValue -> quiz)
                .orElseThrow(() -> new UnsupportedOperationException("Batch code is null"));
    }

    private StudentQuizDetail createStudentQuizDetailAndSave(StudentQuiz studentQuiz) {
        return studentQuizDetailService.createStudentQuizDetail(studentQuiz, null);
    }

    @Override
    public Quiz copyQuizWithTargetBatch(Batch targetBatch, Quiz quiz) {
        return Optional.ofNullable(quiz)
                .map(this::createNewQuiz)
                .map(newQuiz -> {
                    newQuiz.setBatch(targetBatch);
                    return newQuiz;
                })
                .map(newQuiz -> this.createStudentQuizByBatchCode(targetBatch.getCode(), newQuiz))
                .orElseThrow(() -> new UnsupportedOperationException("Copy Quiz Failed"));
    }

    private Quiz createNewQuiz(Quiz quiz) {
        Quiz newQuiz = new Quiz();
        BeanUtils.copyProperties(quiz, newQuiz, "_id", "id");
        return newQuiz;
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
