package com.future.function.validation.validator.scoring;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.validation.annotation.scoring.ExtensionMustBeValid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintValidatorContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.annotation.AnnotationParser;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExtensionMustBeValidValidatorTest {

  private final String[] allowedExtension = {"docx", "pdf"};

  private ExtensionMustBeValid annotation;

  private ConstraintValidatorContext context;

  private final static String FILE_ID = "file-id";
  private final static String FILE_PATH = "/path/name.docx";
  private final static String ANOTHER_FILE_PATH = "/path/name.jpeg";

  private FileV2 fileV2;

  @Mock
  private FileRepositoryV2 fileRepositoryV2;

  @InjectMocks
  private ExtensionMustBeValidValidator extensionMustBeValidValidator;

  @Before
  public void setUp() throws Exception {
    Map<String, Object> annotationMap = new HashMap<>();
    annotationMap.put("extensions", allowedExtension);
    annotation = (ExtensionMustBeValid) AnnotationParser
        .annotationForMap(ExtensionMustBeValid.class, annotationMap);
    extensionMustBeValidValidator.initialize(annotation);


    fileV2 = FileV2.builder()
        .id(FILE_ID)
        .filePath(FILE_PATH)
        .build();

    when(fileRepositoryV2.findOne(FILE_ID)).thenReturn(fileV2);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(fileRepositoryV2);
  }

  @Test
  public void isValidTrue() {
    boolean result = extensionMustBeValidValidator.isValid(Collections.singletonList(FILE_ID), context);
    assertTrue(result);
    verify(fileRepositoryV2).findOne(FILE_ID);
  }

  @Test
  public void isValidFalse() {
    fileV2.setFilePath(ANOTHER_FILE_PATH);
    boolean result = extensionMustBeValidValidator.isValid(Collections.singletonList(FILE_ID), context);
    assertFalse(result);
    verify(fileRepositoryV2).findOne(FILE_ID);
  }

  @Test
  public void isValidWithEmptyFiles() {
    boolean result = extensionMustBeValidValidator.isValid(Collections.emptyList(), context);
    assertTrue(result);
  }

  @Test
  public void isValidWithNullFromRepository() {
    boolean result = extensionMustBeValidValidator.isValid(Collections.singletonList("id"), context);
    assertFalse(result);
    verify(fileRepositoryV2).findOne("id");
  }
}
