package com.future.function.common.validation;

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
  
  @Before
  public void setUp() {}
  
  @BeforeClass
  public static void setUpClass() {
    
    validatorFactory = Validation.buildDefaultValidatorFactory();
    realValidator = validatorFactory.getValidator();
  }
  
  @After
  public void tearDown() {
  
    verifyNoMoreInteractions(validator);
  }
  
  @AfterClass
  public static void tearDownClass() {
    
    validatorFactory.close();
  }
  
  @Test
  public void testGivenValidDataByValidatingDataReturnData() {
    
    dummyData = DummyData.builder()
      .number(11)
      .string("String")
      .build();
    
    when(validator.validate(dummyData)).thenReturn(realValidator.validate(dummyData));
    
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
  
    when(validator.validate(dummyData)).thenReturn(realValidator.validate(dummyData));
    
    try {
      objectValidator.validate(dummyData);
    } catch (BadRequestException e) {
      assertThat(e.getMessage()).isNotBlank();
      assertThat(e.getConstraintViolations()).isNotEmpty();
      assertThat(e.getConstraintViolations()
                   .size()).isEqualTo(2);
    }
    
    verify(validator, times(1)).validate(dummyData);
  }
  
}