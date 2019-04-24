package com.future.function.web.mapper.helper;

import com.future.function.web.dummy.data.DummyData;
import com.future.function.web.model.request.core.UserWebRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;

public class ErrorHelperTest {
  
  private static final DummyData DUMMY_DATA = DummyData.builder()
    .number(7)
    .email(" ")
    .build();
  
  private static final UserWebRequest USER = UserWebRequest.builder()
    .email("email@email.com")
    .name("name")
    .address("address")
    .role("JUDGE")
    .batch(1L)
    .university("university")
    .build();
  
  private static ValidatorFactory validatorFactory;
  
  private static Validator validator;
  
  @BeforeClass
  public static void setUpClass() {
    
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }
  
  @AfterClass
  public static void tearDownClass() {
    
    validatorFactory.close();
  }
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenSetOfConstraintViolationsByMappingErrorsReturnMapOfErrors() {
  
    catchException(() -> throwConstraintViolationException(DUMMY_DATA));
  
    Set<ConstraintViolation<?>> violations = caughtException(
      ConstraintViolationException.class).getConstraintViolations();
    Map<String, List<String>> errors = ErrorHelper.toErrors(violations);
  
    assertThat(errors).isNotEmpty();
    assertThat(errors.keySet()
                 .size()).isEqualTo(2);
    assertThat(errors.get("number")).isEqualTo(
      Collections.singletonList("Min"));
    assertThat(errors.get("email")).contains("NotBlank", "Email");
    
  }
  
  private void throwConstraintViolationException(Object object) {
    
    throw new ConstraintViolationException(validator.validate(object));
  }
  
  @Test
  public void testGivenSetOfConstraintViolationsWithNotBlankPropertyPathByMappingErrorsReturnMapOfErrorsWithGettingFieldValue() {
  
    catchException(() -> throwConstraintViolationException(USER));
  
    Set<ConstraintViolation<?>> violations = caughtException(
      ConstraintViolationException.class).getConstraintViolations();
    Map<String, List<String>> errors = ErrorHelper.toErrors(violations);
  
    assertThat(errors).isNotEmpty();
    assertThat(errors.keySet()
                 .size()).isEqualTo(1);
    assertThat(errors.get("role")).isEqualTo(
      Collections.singletonList("OnlyStudentCanHaveBatchAndUniversity"));
  }
  
}
