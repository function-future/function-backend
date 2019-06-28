package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuestionRepository;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.service.api.feature.scoring.StudentQuestionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
  public List<Question> findAllQuestionsFromMultipleQuestionBank(boolean random, List<QuestionBank> questionBanks,
      int questionCount) {
    return Optional.of(questionBanks)
        .filter(list -> !list.isEmpty())
        .map(this::getIdFromQuestionBanks)
        .map(questionService::findAllByMultipleQuestionBankId)
        .map(questionList -> getListOfQuestions(random, questionCount, questionList))
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
    List<StudentQuestion> studentQuestionList = new ArrayList<>();
    for (int i = 0; i < questionList.size(); i++) {
      Question question = questionList.get(i);
      StudentQuestion studentQuestion = StudentQuestion
          .builder()
          .question(question)
          .option(findCorrectOptionFromQuestion(question))
          .number(i + 1)
          .studentQuizDetail(studentQuizDetail).build();
      studentQuestionList.add(studentQuestion);
    }
    return studentQuestionList;
  }

  private Option findCorrectOptionFromQuestion(Question question) {
    return question
        .getOptions()
        .stream()
        .filter(Option::isCorrect)
        .findFirst()
        .orElseThrow(() -> new NotFoundException("No Correct Option"));
  }

  private List<Question> getListOfQuestions(boolean random, int questionCount, List<Question> questionList) {
    if (random)
      Collections.shuffle(questionList);
    if (questionList.size() < questionCount)
      questionCount = questionList.size();
    return questionList.subList(0, questionCount);
  }

  @Override
  public Integer postAnswerForAllStudentQuestion(List<StudentQuestion> answers) {
    String studentQuizDetailId = validateAnswersForSameAndReturnStudentQuizDetailId(answers);
    List<StudentQuestion> questions = this.findAllByStudentQuizDetailId(studentQuizDetailId);
    Long correctQuestions = questions.stream()
        .filter(question -> checkRequestedOptionCorrect(answers, question))
        .map(question -> setCorrectOption(answers, questions, question))
        .map(studentQuestionRepository::save)
        .count();
    return getTotalPoint(questions, correctQuestions);
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
    return (Integer.valueOf(String.valueOf(correctQuestions)) / questions.size()) * 100;
  }

  @Override
  public List<StudentQuestion> createStudentQuestionsByStudentQuizDetail(StudentQuizDetail studentQuizDetail,
      List<StudentQuestion> studentQuestions) {
    for (int i = 0; i < studentQuestions.size(); i++) {
      studentQuestions.get(i).setStudentQuizDetail(studentQuizDetail);
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
        .map(question -> {
          question.setDeleted(true);
          return question;
        })
        .forEach(studentQuestionRepository::save);
  }
}
