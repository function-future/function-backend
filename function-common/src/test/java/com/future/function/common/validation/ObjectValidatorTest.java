package com.future.function.common.validation;

import com.future.function.common.dummy.DummyData;
import com.future.function.common.exception.BadRequestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ObjectValidatorTest {
  
  private static ValidatorFactory validatorFactory;
  
  private static Validator realValidator;
  
  @InjectMocks
  private ObjectValidator objectValidator;
  
  @Mock
  private Validator validator;
  
  private DummyData dummyData;
  
  @BeforeClass
  public static void setUpClass() {
    
    validatorFactory = Validation.buildDefaultValidatorFactory();
    realValidator = validatorFactory.getValidator();
  }
  
  @AfterClass
  public static void tearDownClass() {
    
    validatorFactory.close();
  }
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenValidDataByValidatingDataReturnData() {
    
    dummyData = DummyData.builder()
      .number(11)
      .string("String")
      .build();
    
    when(validator.validate(dummyData)).thenReturn(
      realValidator.validate(dummyData));
    
    DummyData validatedDummyData = objectValidator.validate(dummyData);
    
    assertThat(validatedDummyData).isNotNull();
    assertThat(validatedDummyData.getNumber()).isEqualTo(11);
    assertThat(validatedDummyData.getString()).isEqualTo("String");
    
    verify(validator, times(1)).validate(dummyData);
  }
  
  @Test
  public void testGivenInvalidDataByValidatingDataReturnBadRequestException() {
    
    dummyData = DummyData.builder()
      .number(7)
      .string("")
      .build();
    
    when(validator.validate(dummyData)).thenReturn(
      realValidator.validate(dummyData));
  
    catchException(() -> objectValidator.validate(dummyData));
  
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
    assertThat(caughtException().getMessage()).isNotBlank();
    assertThat(caughtException(
      BadRequestException.class).getConstraintViolations()).isNotEmpty();
    assertThat(caughtException(
      BadRequestException.class).getConstraintViolations()
                 .size()).isEqualTo(2);
    
    verify(validator, times(1)).validate(dummyData);
  }
  
}
