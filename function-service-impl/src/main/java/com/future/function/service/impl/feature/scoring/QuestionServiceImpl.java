package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.repository.feature.scoring.QuestionRepository;
import com.future.function.service.api.feature.scoring.OptionService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionRepository questionRepository;

    private OptionService optionService;

    private QuestionBankService questionBankService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, OptionService optionService,
                               QuestionBankService questionBankService) {
        this.questionRepository = questionRepository;
        this.optionService = optionService;
        this.questionBankService = questionBankService;
    }

    @Override
    public Page<Question> findAllByQuestionBankId(String questionBankId, Pageable pageable) {
        return Optional.ofNullable(questionBankId)
                .map(id -> questionRepository.findAllByQuestionBankId(id, pageable))
                .map(this::findListFromQuestionPage)
                .orElseThrow(() -> new NotFoundException("Question Bank is not found"));
    }

    private Page<Question> findListFromQuestionPage(Page<Question> page) {
        page.getContent()
                .forEach(this::searchOptionsForQuestion);
        return page;
    }

    private void searchOptionsForQuestion(Question question) {
        question.setOptions(
                optionService
                        .getOptionListByQuestionId(question.getId()
                        )
        );
    }

    @Override
    public Question findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(questionRepository::findByIdAndDeletedFalse)
                .orElseThrow(() -> new NotFoundException("Question not found"));
    }

    @Override
    public Question createQuestion(Question question, String questionBankId) {
        question.setQuestionBank(getQuestionBank(questionBankId));
        List<Option> options = question.getOptions();
        saveOptionList(question, options, true);
        question.setOptions(null);
        question = questionRepository.save(question);
        question.setOptions(options);
        return question;
    }

    private QuestionBank getQuestionBank(String questionBankId) {
        return questionBankService.findById(questionBankId);
    }

    private void saveOptionList(Question question, List<Option> options, boolean isCreate) {
        options
                .stream()
                .peek(option -> option.setQuestion(question))
                .forEach(isCreate ? optionService::createOption : optionService::updateOption);
    }

    @Override
    public Question updateQuestion(Question question, String questionBankId) {
        question.setQuestionBank(getQuestionBank(questionBankId));
        List<Option> options = question.getOptions();
        saveOptionList(question, options, false);
        return Optional.of(question)
                .map(Question::getId)
                .flatMap(questionRepository::findByIdAndDeletedFalse)
                .map(foundQuestion -> convertAndSaveQuestion(question, options, foundQuestion))
                .orElse(question);
    }

    private Question convertAndSaveQuestion(Question question, List<Option> options, Question foundQuestion) {
        BeanUtils.copyProperties(question, foundQuestion);
        foundQuestion.setOptions(null);
        foundQuestion = questionRepository.save(foundQuestion);
        foundQuestion.setOptions(options);
        return foundQuestion;
    }

    @Override
    public void deleteById(String id) {
        Optional.of(id)
                .flatMap(questionRepository::findByIdAndDeletedFalse)
                .ifPresent(question -> {
                    deleteAllOptionRelatedToQuestion(question);
                    question.setDeleted(true);
                    questionRepository.save(question);
                });
    }

    private void deleteAllOptionRelatedToQuestion(Question question) {
        optionService.getOptionListByQuestionId(question.getId())
                .forEach(option -> optionService.deleteById(option.getId()));
    }
}
