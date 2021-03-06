package com.future.function.validation.validator.core;

import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.validation.annotation.core.FileMustBeImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileMustBeImageValidator
  implements ConstraintValidator<FileMustBeImage, List<String>> {

  private static final String DOT = ".";

  @Autowired
  private FileProperties fileProperties;

  @Autowired
  private FileRepositoryV2 fileRepositoryV2;

  @Override
  public void initialize(FileMustBeImage constraintAnnotation) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(
    List<String> value, ConstraintValidatorContext context
  ) {

    return Optional.ofNullable(value)
      .orElseGet(Collections::emptyList)
      .stream()
      .map(fileRepositoryV2::findByIdAndDeletedFalse)
      .allMatch(this::hasRecognizedImageExtension);
  }

  private Boolean hasRecognizedImageExtension(Optional<FileV2> retrievedFile) {

    return retrievedFile.map(this::getFileExtension)
      .map(ext -> fileProperties.getImageExtensions()
        .contains(DOT + ext.toLowerCase()))
      .orElse(Boolean.FALSE);
  }

  private String getFileExtension(FileV2 retrievedFile) {

    return Optional.ofNullable(retrievedFile)
      .map(FileV2::getFilePath)
      .map(FilenameUtils::getExtension)
      .orElse(null);
  }

}
