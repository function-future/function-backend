package com.future.function.web.mapper.helper;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.dummy.data.DummyData;
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

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorHelperTest {
  
  private static final DummyData DUMMY_DATA = DummyData.builder()
    .number(7)
    .email(" ")
    .build();
  
  private static final User USER = User.builder()
    .email("email@email.com")
    .name("name")
    .address("address")
    .role(Role.JUDGE)
    .batch(Batch.builder()
             .number(1)
             .build())
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
    
    try {
      throw new ConstraintViolationException(validator.validate(DUMMY_DATA));
    } catch (ConstraintViolationException e) {
      Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
      Map<String, List<String>> errors = ErrorHelper.toErrors(violations);
      
      assertThat(errors).isNotEmpty();
      assertThat(errors.keySet()
                   .size()).isEqualTo(2);
      assertThat(errors.get("number")).isEqualTo(
        Collections.singletonList("Min"));
      assertThat(errors.get("email")).contains("NotBlank", "Email");
    }
  }
  
  @Test
  public void testGivenSetOfConstraintViolationsWithNotBlankPropertyPathByMappingErrorsReturnMapOfErrorsWithGettingFieldValue() {
    
    try {
      throw new ConstraintViolationException(validator.validate(USER));
    } catch (ConstraintViolationException e) {
      Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
      Map<String, List<String>> errors = ErrorHelper.toErrors(violations);
      
      assertThat(errors).isNotEmpty();
      assertThat(errors.keySet()
                   .size()).isEqualTo(1);
      assertThat(errors.get("role")).isEqualTo(
        Collections.singletonList("OnlyStudentCanHaveBatchAndUniversity"));
    }
  }
  
}
