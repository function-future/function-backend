package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.web.model.request.scoring.OptionWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OptionRequestMapper {

    private ObjectValidator validator;

    @Autowired
    public OptionRequestMapper(ObjectValidator validator) {
        this.validator = validator;
    }

    public Option toOption(OptionWebRequest request) {
        return Optional.ofNullable(request)
                .map(this::toValidatedOption)
                .orElseThrow(() -> new BadRequestException("Bad Request"));
    }

    private Option toValidatedOption(OptionWebRequest request) {
        request = validator.validate(request);
        return Option.builder()
                .id(request.getId())
                .label(request.getLabel())
                .correct(request.getCorrect() != null && request.getCorrect().equals(true))
                .build();
    }

}
