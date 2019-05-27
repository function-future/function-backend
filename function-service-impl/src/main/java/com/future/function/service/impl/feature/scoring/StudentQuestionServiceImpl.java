package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuestionRepository;
import com.future.function.service.api.feature.scoring.StudentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentQuestionServiceImpl implements StudentQuestionService {

    @Autowired
    private StudentQuestionRepository studentQuestionRepository;

    @Override
    public List<StudentQuestion> findAllByStudentQuizDetailId(String studentQuizDetailId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "number");

        return Optional.ofNullable(studentQuizDetailId)
                .map(id -> studentQuestionRepository.findAllByStudentQuizDetailId(id, sort))
                .orElseGet(ArrayList::new);
    }

    @Override
    public Integer postAnswerForAllStudentQuestion(List<StudentQuestion> answers) {
        String studentQuizDetailId = validateAnswersForSameAndReturnStudentQuizDetailId(answers);

        List<StudentQuestion> questions = this.findAllByStudentQuizDetailId(studentQuizDetailId);

        Long correctQuestions = questions.stream()
                .filter(question -> checkRequestedOptionCorrect(answers, question))
                .count();
        return getTotalPoint(questions, correctQuestions);
    }

    private boolean checkRequestedOptionCorrect(List<StudentQuestion> answers, StudentQuestion question) {
        return question
                .getOption()
                .getId()
                .equals(getAnswerIdFromAnswerList(answers, question));
    }

    private String getAnswerIdFromAnswerList(List<StudentQuestion> answers, StudentQuestion question) {
        return answers.get(question.getNumber()).getOption().getId();
    }

    private String validateAnswersForSameAndReturnStudentQuizDetailId(List<StudentQuestion> answers) {
        String studentQuizDetailId = answers.get(0).getStudentQuizDetail().getId();
        answers
                .forEach(answer -> {
                    if (!answer.getStudentQuizDetail().getId().equals(studentQuizDetailId))
                        throw new UnsupportedOperationException("Student quiz detail id not equals");
                });
        return studentQuizDetailId;
    }

    private int getTotalPoint(List<StudentQuestion> questions, Long correctQuestions) {
        return Integer.valueOf(String.valueOf(correctQuestions)) / questions.size();
    }

    @Override
    public List<StudentQuestion> createStudentQuestionsByStudentQuizDetail(StudentQuizDetail studentQuizDetail,
                                                                           List<StudentQuestion> studentQuestions) {
        for (int i = 0; i < studentQuestions.size(); i++) {
            studentQuestions.get(0).setStudentQuizDetail(studentQuizDetail);
            studentQuestions.get(i).setNumber(i + 1);
        }

        return studentQuestions
                .stream()
                .map(studentQuestionRepository::save)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByStudentQuizDetailId(String studentQuizDetailId) {
        Optional.ofNullable(studentQuizDetailId)
                .map(this::findAllByStudentQuizDetailId)
                .ifPresent(this::safeDeleteStudentQuestionList);
    }

    private void safeDeleteStudentQuestionList(List<StudentQuestion> list) {
        list.stream()
                .peek(question -> question.setDeleted(true))
                .forEach(studentQuestionRepository::save);
    }
}
