package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.repository.feature.scoring.QuestionRepository;
import com.future.function.service.api.feature.scoring.OptionService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

  private QuestionRepository questionRepository;

  private OptionService optionService;

  private QuestionBankService questionBankService;

  @Autowired
  public QuestionServiceImpl(
    QuestionRepository questionRepository, OptionService optionService,
    QuestionBankService questionBankService
  ) {

    this.questionRepository = questionRepository;
    this.optionService = optionService;
    this.questionBankService = questionBankService;
  }

  @Override
  public Page<Question> findAllByQuestionBankId(
    String questionBankId, Pageable pageable
  ) {

    return Optional.ofNullable(questionBankId)
      .map(id -> questionRepository.findAllByQuestionBankIdAndDeletedFalse(id,
                                                                           pageable
      ))
      .map(this::setOptionsInQuestionPage)
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public List<Question> findAllByMultipleQuestionBankId(
    List<String> questionBankIds
  ) {

    return Optional.ofNullable(questionBankIds)
      .map(this::findQuestionsFromQuestionBanks)
      .map(this::setOptionsForQuestions)
      .orElseGet(ArrayList::new);
  }

  private List<Question> findQuestionsFromQuestionBanks(List<String> list) {

    List<Question> questionList = new ArrayList<>();
    list.stream()
      .map(questionRepository::findAllByQuestionBankIdAndDeletedFalse)
      .forEach(questionList::addAll);
    return questionList;
  }

  private List<Question> setOptionsForQuestions(List<Question> list) {

    list.forEach(this::searchOptionsForQuestion);
    return list;
  }

  @Override
  public Question findById(String id) {

    return Optional.ofNullable(id)
      .flatMap(questionRepository::findByIdAndDeletedFalse)
      .map(this::searchOptionsForQuestion)
      .orElseThrow(
        () -> new NotFoundException("Failed at #findById #QuestionService"));
  }

  @Override
  public Question createQuestion(Question question, String questionBankId) {

    List<Option> options = question.getOptions();
    return Optional.ofNullable(questionBankId)
      .map(questionBankService::findById)
      .map(questionBank -> setQuestionBank(question, questionBank))
      .map(currentQuestion -> saveOptionsAndSetNullOptionsForQuestion(options,
                                                                      currentQuestion,
                                                                      true
      ))
      .map(questionRepository::save)
      .map(currentQuestion -> setOptionForCurrentQuestion(currentQuestion,
                                                          options
      ))
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #createQuestion #QuestionService"));
  }

  private Question saveOptionsAndSetNullOptionsForQuestion(
    List<Option> optionList, Question currentQuestion, boolean createOption
  ) {

    saveOptionList(currentQuestion, optionList, createOption);
    return setOptionForCurrentQuestion(currentQuestion, null);
  }

  private Question setOptionForCurrentQuestion(
    Question currentQuestion, List<Option> o
  ) {

    currentQuestion.setOptions(o);
    return currentQuestion;
  }

  private void saveOptionList(
    Question question, List<Option> options, boolean isCreate
  ) {

    options.stream()
      .map(option -> {
        option.setQuestion(question);
        return option;
      })
      .forEach(
        isCreate ? optionService::createOption : optionService::updateOption);
  }

  private Question setQuestionBank(
    Question question, QuestionBank questionBank
  ) {

    question.setQuestionBank(questionBank);
    return question;
  }

  @Override
  public Question updateQuestion(Question question, String questionBankId) {

    List<Option> options = question.getOptions();
    return Optional.ofNullable(questionBankId)
      .map(questionBankService::findById)
      .map(questionBank -> setQuestionBank(question, questionBank))
      .map(currentQuestion -> saveOptionsAndSetNullOptionsForQuestion(options,
                                                                      currentQuestion,
                                                                      false
      ))
      .map(Question::getId)
      .flatMap(questionRepository::findByIdAndDeletedFalse)
      .map(foundQuestion -> copyPropertiesOfQuestionRequest(question,
                                                            foundQuestion
      ))
      .map(questionRepository::save)
      .map(currentQuestion -> setOptionForCurrentQuestion(currentQuestion,
                                                          options
      ))
      .orElse(question);
  }

  private Question copyPropertiesOfQuestionRequest(
    Question question, Question foundQuestion
  ) {

    CopyHelper.copyProperties(question, foundQuestion);
    return foundQuestion;
  }

  @Override
  public void deleteById(String id) {

    Optional.of(id)
      .flatMap(questionRepository::findByIdAndDeletedFalse)
      .ifPresent(this::setDeletedAndSaveQuestion);
  }

  private void setDeletedAndSaveQuestion(Question question) {

    deleteAllOptionRelatedToQuestion(question);
    question.setDeleted(true);
    questionRepository.save(question);
  }

  private void deleteAllOptionRelatedToQuestion(Question question) {

    optionService.getOptionListByQuestionId(question.getId())
      .forEach(option -> optionService.deleteById(option.getId()));
  }

  private Page<Question> setOptionsInQuestionPage(Page<Question> page) {

    page.getContent()
      .forEach(this::searchOptionsForQuestion);
    return page;
  }

  private Question searchOptionsForQuestion(Question question) {

    question.setOptions(
      optionService.getOptionListByQuestionId(question.getId()));
    return question;
  }

}
