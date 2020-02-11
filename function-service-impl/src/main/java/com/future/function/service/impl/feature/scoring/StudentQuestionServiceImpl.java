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
  public StudentQuestionServiceImpl(
    StudentQuestionRepository studentQuestionRepository,
    QuestionService questionService
  ) {

    this.studentQuestionRepository = studentQuestionRepository;
    this.questionService = questionService;
  }

  @Override
  public List<StudentQuestion> findAllByStudentQuizDetailId(
    String studentQuizDetailId
  ) {

    return Optional.ofNullable(studentQuizDetailId)
      .map(
        studentQuestionRepository::findAllByStudentQuizDetailIdAndDeletedFalseOrderByNumberAsc)
      .orElseGet(ArrayList::new);
  }

  @Override
  public List<Question> findAllRandomQuestionsFromMultipleQuestionBank(
    List<QuestionBank> questionBanks, int questionCount
  ) {

    return Optional.of(questionBanks)
      .filter(list -> !list.isEmpty())
      .map(this::getIdFromQuestionBanks)
      .map(questionService::findAllByMultipleQuestionBankId)
      .map(questionList -> getListOfQuestions(questionCount, questionList))
      .orElseGet(ArrayList::new);
  }

  private List<String> getIdFromQuestionBanks(List<QuestionBank> list) {

    return list.stream()
      .map(QuestionBank::getId)
      .collect(Collectors.toList());
  }

  private List<Question> getListOfQuestions(
      int questionCount, List<Question> questionList
  ) {

    Collections.shuffle(questionList);
    if (questionList.size() < questionCount) {
      questionCount = questionList.size();
    }
    return questionList.subList(0, questionCount);
  }

  @Override
  public List<StudentQuestion> createStudentQuestionsFromQuestionList(
    List<Question> questionList, StudentQuizDetail studentQuizDetail
  ) {

    return IntStream.range(0, questionList.size())
      .mapToObj(i -> Pair.of(i + 1, questionList.get(i)))
      .map(pair -> toStudentQuestion(studentQuizDetail, pair))
      .map(studentQuestionRepository::save)
      .collect(Collectors.toList());
  }

  private StudentQuestion toStudentQuestion(
    StudentQuizDetail studentQuizDetail, Pair<Integer, Question> pair
  ) {
    Question question = pair.getSecond();
    int number = pair.getFirst();
    return StudentQuestion.builder()
      .question(question)
      .option(findCorrectOptionFromQuestion(question))
      .number(number)
      .studentQuizDetail(studentQuizDetail)
      .build();
  }

  private Option findCorrectOptionFromQuestion(Question question) {

    return question.getOptions()
      .stream()
      .filter(Option::isCorrect)
      .findFirst()
      .orElseThrow(() -> new NotFoundException(
        "Failed at #FindCorrectOption #StudentQuestionService"));
  }

  @Override
  public Integer postAnswerForAllStudentQuestion(
    List<StudentQuestion> answers, String studentQuizDetailId
  ) {

    return Optional.ofNullable(studentQuizDetailId)
      .map(this::findAllByStudentQuizDetailId)
      .map(questions -> getCorrectQuestionsCount(answers, questions))
      .map(pair -> getTotalPoint(pair.getFirst(), pair.getSecond()))
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #postAnswerForAllStudentQuestion #StudentQuestionService"));
  }

  private Pair<Integer, Long> getCorrectQuestionsCount(
    List<StudentQuestion> answers, List<StudentQuestion> questionList
  ) {

    Long correctQuestionsCount = questionList.stream()
      .map(question -> checkAndSetCorrectStatus(answers, question))
      .map(studentQuestionRepository::save)
      .filter(StudentQuestion::isCorrect)
      .count();
    return Pair.of(questionList.size(), correctQuestionsCount);
  }

  private StudentQuestion checkAndSetCorrectStatus(
      List<StudentQuestion> answers, StudentQuestion question
  ) {

    boolean correct = question.getOption()
        .getId()
        .equals(getAnswerIdFromAnswerList(answers, question));

    question.setCorrect(correct);
    return question;
  }

  private String getAnswerIdFromAnswerList(
      List<StudentQuestion> answers, StudentQuestion question
  ) {

    return answers.get(question.getNumber() - 1).getOption().getId();
  }

  private int getTotalPoint(
    Integer questionCount, Long correctQuestions
  ) {

    return (int) ((correctQuestions.floatValue() / questionCount) * 100);
  }

  @Override
  public void deleteAllByStudentQuizDetailId(String studentQuizDetailId) {

    Optional.ofNullable(studentQuizDetailId)
      .map(this::findAllByStudentQuizDetailId)
      .ifPresent(this::safeDeleteStudentQuestionList);
  }

  private void safeDeleteStudentQuestionList(List<StudentQuestion> list) {

    list.forEach(question -> {
        question.setDeleted(true);
        studentQuestionRepository.save(question);
      });

  }

}
