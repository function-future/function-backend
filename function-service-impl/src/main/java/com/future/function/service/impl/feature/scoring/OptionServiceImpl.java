package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.repository.feature.scoring.OptionRepository;
import com.future.function.service.api.feature.scoring.OptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionServiceImpl implements OptionService {

    private OptionRepository optionRepository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public List<Option> getOptionListByQuestionId(String questionId) {
        return Optional.ofNullable(questionId)
                .map(optionRepository::findAllByQuestionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));
    }

    @Override
    public Option findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(optionRepository::findByIdAndDeletedFalse)
                .orElseThrow(() -> new NotFoundException("Option not found"));
    }

    @Override
    public Option createOption(Option option) {
        return optionRepository.save(option);
    }

    @Override
    public Option updateOption(Option option) {
        return Optional.of(option)
                .map(Option::getId)
                .flatMap(optionRepository::findByIdAndDeletedFalse)
                .map(oldOption -> {
                    BeanUtils.copyProperties(option, oldOption);
                    return optionRepository.save(oldOption);
                })
                .orElse(option);
    }

    @Override
    public boolean isOptionCorrect(Option option) {
        return Optional.of(option)
                .map(Option::getId)
                .map(this::findById)
                .map(optionDB -> option.isCorrect() == optionDB.isCorrect())
                .orElse(false);
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(id)
                .flatMap(optionRepository::findByIdAndDeletedFalse)
                .ifPresent(option -> {
                    option.setDeleted(true);
                    optionRepository.save(option);
                });
    }
}
