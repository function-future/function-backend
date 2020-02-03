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
    List<String> extensionList = Arrays.asList(extensions);
    if(Objects.isNull(value) || value.isEmpty()) return true;
    return value.stream()
        .allMatch(fileId -> {
          FileV2 fileV2 = fileRepositoryV2.findOne(fileId);
          if(Objects.nonNull(fileV2)) {
            String extension = FilenameUtils.getExtension(fileV2.getFilePath());
            return extensionList.contains(extension.toLowerCase());
          }
          return Boolean.FALSE;
        });
  }
}
