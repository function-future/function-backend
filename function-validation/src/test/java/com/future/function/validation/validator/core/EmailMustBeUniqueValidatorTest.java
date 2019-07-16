package com.future.function.validation.validator.core;

import com.future.function.common.data.core.UserData;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailMustBeUniqueValidatorTest {
  
  private static final String EMAIL = "email";
  
  private static final String ID = "id";
  
  @Mock
  private EmailMustBeUnique annotation;
  
  @Mock
  private UserData userData;
  
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
  public void testGivenUniqueEmailWhenCreatingUserByValidatingEmailIsUniqueInDatabaseReturnTrue() {

    when(userData.getId()).thenReturn(null);
    when(userData.getEmail()).thenReturn(EMAIL);
    
    when(userRepository.findByEmailAndDeletedFalse(EMAIL)).thenReturn(Optional.empty());
  
    assertThat(validator.isValid(userData, null)).isTrue();
    
    verify(userData).getId();
    verify(userData).getEmail();
    
    verify(userRepository).findByEmailAndDeletedFalse(EMAIL);
  }
  
  @Test
  public void testGivenExistingEmailWhenCreatingUserByValidatingEmailIsUniqueInDatabaseReturnFalse() {

    when(userData.getId()).thenReturn(null);
    when(userData.getEmail()).thenReturn(EMAIL);
  
    when(userRepository.findByEmailAndDeletedFalse(EMAIL)).thenReturn(
      Optional.of(new User()));
  
    assertThat(validator.isValid(userData, null)).isFalse();
    
    verify(userData).getId();
    verify(userData).getEmail();
    
    verify(userRepository).findByEmailAndDeletedFalse(EMAIL);
  }
  
  @Test
  public void testGivenUniqueEmailWhenUpdateUserByValidatingEmailIsUniqueInDatabaseReturnTrue() {
  
    when(userData.getId()).thenReturn(ID);
    when(userData.getEmail()).thenReturn(EMAIL);
    
    when(userRepository.findByEmailAndDeletedFalse(EMAIL)).thenReturn(Optional.of(
      User.builder().id(ID).build()));
  
    assertThat(validator.isValid(userData, null)).isTrue();
    
    verify(userData, times(2)).getId();
    verify(userData).getEmail();
    
    verify(userRepository).findByEmailAndDeletedFalse(EMAIL);
  }
  
  @Test
  public void testGivenExistingEmailWhenUpdateUserByValidatingEmailIsUniqueInDatabaseReturnFalse() {
    
    when(userData.getId()).thenReturn(ID);
    when(userData.getEmail()).thenReturn(EMAIL);
    
    when(userRepository.findByEmailAndDeletedFalse(EMAIL)).thenReturn(Optional.of(
      User.builder().id(ID + "-1").build()));
  
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData, times(2)).getId();
    verify(userData).getEmail();
    
    verify(userRepository).findByEmailAndDeletedFalse(EMAIL);
  }
  
}