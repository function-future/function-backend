package com.future.function.validation.validator.communication;

import com.future.function.validation.annotation.communication.ChatroomName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ChatroomNameValidatorTest {

  private static final String VALID_NAME = "Chatroom Name 123";

  private static final String INVALID_NAME =
    "Chatroom Name 123 name name name name name";

  @Mock
  private ChatroomName annotation;

  @InjectMocks
  private ChatroomNameValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation);
  }

  @Test
  public void testGivenValidNameByValidatingNameReturnTrue() {

    assertThat(validator.isValid(VALID_NAME, null)).isTrue();
  }

  @Test
  public void testGivenInvalidNameByValidatingNameReturnFalse() {

    assertThat(validator.isValid(INVALID_NAME, null)).isFalse();
  }

}
