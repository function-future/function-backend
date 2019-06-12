package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Phone;
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
public class PhoneValidatorTest {

  private static final String VALID_PHONE_11 = "08121234123";

  private static final String VALID_PHONE_12 = "081212341234";

  private static final String VALID_PHONE_13 = "0812123412345";

  private static final String VALID_PHONE_62_11 = "+628121234123";

  private static final String VALID_PHONE_62_12 = "+6281212341234";

  private static final String VALID_PHONE_62_13 = "+62812123412345";

  private static final String INVALID_PHONE = "invalid-phone";

  @Mock
  private Phone annotation;

  @InjectMocks
  private PhoneValidator validator;

  @Before
  public void setUp() {

    validator.initialize(annotation);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(annotation);
  }

  @Test
  public void testGivenValidStringAsPhoneByValidatingPhoneReturnTrue() {

    assertThat(validator.isValid(VALID_PHONE_11, null)).isTrue();
    assertThat(validator.isValid(VALID_PHONE_12, null)).isTrue();
    assertThat(validator.isValid(VALID_PHONE_13, null)).isTrue();
    assertThat(validator.isValid(VALID_PHONE_62_11, null)).isTrue();
    assertThat(validator.isValid(VALID_PHONE_62_12, null)).isTrue();
    assertThat(validator.isValid(VALID_PHONE_62_13, null)).isTrue();
  }

  @Test
  public void testGivenNullStringAsPhoneByValidatingPhoneReturnTrue() {

    assertThat(validator.isValid(null, null)).isTrue();
  }

  @Test
  public void testGivenInvalidStringAsPhoneByValidatingPhoneReturnFalse() {

    assertThat(validator.isValid(INVALID_PHONE, null)).isFalse();
  }

}
