package com.future.function.validation.validator.core;

import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.validation.annotation.core.EmailMustBeUnique;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailMustBeUniqueValidatorTest {
  
  private static final String EMAIL = "email";
  
  @Mock
  private EmailMustBeUnique annotation;
  
  @Mock
  private UserRepository userRepository;
  
  @InjectMocks
  private EmailMustBeUniqueValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, userRepository);
  }
  
  @Test
  public void testGivenUniqueEmailByValidatingEmailIsUniqueInDatabaseReturnTrue() {
    
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
  
    assertThat(validator.isValid(EMAIL, null)).isTrue();
    
    verify(userRepository).findByEmail(EMAIL);
  }
  
  @Test
  public void testGivenExistingEmailByValidatingEmailIsUniqueInDatabaseReturnFalse() {
    
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new User()));
  
    assertThat(validator.isValid(EMAIL, null)).isFalse();
    
    verify(userRepository).findByEmail(EMAIL);
  }
  
}