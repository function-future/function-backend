package com.future.function.validation.validator.scoring;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.validation.annotation.scoring.ExtensionMustBeValid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtensionMustBeValidValidator implements ConstraintValidator<ExtensionMustBeValid, List<String>> {

  @Autowired
  private FileRepositoryV2 fileRepositoryV2;

  private String[] extensions;

  @Override
  public void initialize(ExtensionMustBeValid constraintAnnotation) {
    extensions = constraintAnnotation.extensions();
  }

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    if(Objects.isNull(value) || value.isEmpty()) return true;
    return Optional.ofNullable(value)
      .map(list -> list.get(0))
      .map(fileRepositoryV2::findOne)
      .map(FileV2::getFilePath)
      .map(FilenameUtils::getExtension)
      .map(extension -> Arrays.asList(extensions).contains(extension))
      .orElse(false);
  }
}
