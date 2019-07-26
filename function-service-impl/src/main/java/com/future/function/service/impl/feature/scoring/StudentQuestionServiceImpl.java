package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.*;
import com.future.function.repository.feature.scoring.StudentQuestionRepository;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.service.api.feature.scoring.StudentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StudentQuestionServiceImpl implements StudentQuestionService {

  private StudentQuestionRepository studentQuestionRepository;
  private QuestionService questionService;

  @Autowired
  public StudentQuestionServiceImpl(StudentQuestionRepository studentQuestionRepository, QuestionService questionService) {
    this.studentQuestionRepository = studentQuestionRepository;
    this.questionService = questionService;
  }

  @Override
  public List<StudentQuestion> findAllByStudentQuizDetailId(String studentQuizDetailId) {
    return Optional.ofNullable(studentQuizDetailId)
        .map(studentQuestionRepository::findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc)
        .orElseGet(ArrayList::new);
  }

  @Override
  public List<Question> findAllRandomQuestionsFromMultipleQuestionBank(List<QuestionBank> questionBanks,
                                                                       int questionCount) {
    return Optional.of(questionBanks)
        .filter(list -> !list.isEmpty())
        .map(this::getIdFromQuestionBanks)
        .map(questionService::findAllByMultipleQuestionBankId)
            .map(questionList -> getListOfQuestions(questionCount, questionList))
        .orElseGet(ArrayList::new);
  }

  private List<String> getIdFromQuestionBanks(List<QuestionBank> list) {
    return list
        .stream()
        .map(QuestionBank::getId)
        .collect(Collectors.toList());
  }

  @Override
  public List<StudentQuestion> createStudentQuestionsFromQuestionList(List<Question> questionList, StudentQuizDetail studentQuizDetail) {
    return IntStream.range(0, questionList.size())
        .mapToObj(i -> Pair.of(i, questionList.get(i)))
        .map(pair -> mapStudentQuizDetail(studentQuizDetail, pair))
        .map(studentQuestionRepository::save)
        .collect(Collectors.toList());
  }

  private StudentQuestion mapStudentQuizDetail(StudentQuizDetail studentQuizDetail, Pair<Integer, Question> pair) {
    return StudentQuestion
        .builder()
        .question(pair.getSecond())
        .option(findCorrectOptionFromQuestion(pair.getSecond()))
        .number(pair.getFirst() + 1)
        .studentQuizDetail(studentQuizDetail).build();
  }

  private Option findCorrectOptionFromQuestion(Question question) {
    return question
        .getOptions()
        .stream()
        .filter(Option::isCorrect)
        .findFirst()
            .orElseThrow(() -> new NotFoundException("Failed at #FindCorrectOption #StudentQuestionService"));
  }

  private List<Question> getListOfQuestions(int questionCount, List<Question> questionList) {
    Collections.shuffle(questionList);
    if (questionList.size() < questionCount)
      questionCount = questionList.size();
    return questionList.subList(0, questionCount);
  }

  @Override
  public Integer postAnswerForAllStudentQuestion(List<StudentQuestion> answers, String studentQuizDetailId) {
    return Optional.ofNullable(studentQuizDetailId)
            .map(this::findAllByStudentQuizDetailId)
            .map(questions -> getCorrectQuestionsCount(answers, questions))
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #postAnswerForAllStudentQuestion #StudentQuestionService"));
  }

  private int getCorrectQuestionsCount(List<StudentQuestion> answers, List<StudentQuestion> questions) {
    Long correctQuestionsCount = questions.stream()
        .filter(question -> checkRequestedOptionCorrect(answers, question))
        .map(question -> setCorrectOption(answers, questions, question))
        .map(studentQuestionRepository::save)
        .count();
    return getTotalPoint(questions, correctQuestionsCount);
  }

  private StudentQuestion setCorrectOption(List<StudentQuestion> answers, List<StudentQuestion> questions, StudentQuestion question) {
    question.setCorrect(true);
    question.setOption(answers.get(questions.size() - 1).getOption());
    return question;
  }

  private boolean checkRequestedOptionCorrect(List<StudentQuestion> answers, StudentQuestion question) {
    return question
        .getOption()
        .getId()
        .equals(getAnswerIdFromAnswerList(answers, question));
  }

  private String getAnswerIdFromAnswerList(List<StudentQuestion> answers, StudentQuestion question) {
    return answers.get(question.getNumber() - 1).getOption().getId();
  }

  private int getTotalPoint(List<StudentQuestion> questions, Long correctQuestions) {
    return (int) ((correctQuestions.floatValue() / questions.size()) * 100);
  }

  @Override
  public List<StudentQuestion> createStudentQuestionsByStudentQuizDetail(StudentQuizDetail studentQuizDetail,
      List<StudentQuestion> studentQuestions) {
    return IntStream.range(0, studentQuestions.size())
        .mapToObj(i -> Pair.of(i, studentQuestions.get(i)))
        .map(pair -> setStudentQuizDetailAndNumber(studentQuizDetail, pair))
        .map(Pair::getSecond)
        .map(studentQuestionRepository::save)
        .collect(Collectors.toList());
  }

  private Pair<Integer, StudentQuestion> setStudentQuizDetailAndNumber(StudentQuizDetail studentQuizDetail, Pair<Integer, StudentQuestion> pair) {
    pair.getSecond().setStudentQuizDetail(studentQuizDetail);
    pair.getSecond().setNumber(pair.getFirst() + 1);
    return pair;
  }

  @Override
  public void deleteAllByStudentQuizDetailId(String studentQuizDetailId) {
    Optional.ofNullable(studentQuizDetailId)
        .map(this::findAllByStudentQuizDetailId)
        .ifPresent(this::safeDeleteStudentQuestionList);
  }

  private void safeDeleteStudentQuestionList(List<StudentQuestion> list) {
    list.stream()
        .map(question -> {
          question.setDeleted(true);
          return question;
        })
        .forEach(studentQuestionRepository::save);
  }
}
