package com.future.function.web.mapper.request.core;

import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.util.Pair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailRequestMapperTest {
  
  private static final String OLD_PASSWORD = "old-password";
  
  private static final String NEW_PASSWORD = "new-password";
  
  private static final ChangePasswordWebRequest CHANGE_PASSWORD_WEB_REQUEST =
    new ChangePasswordWebRequest(OLD_PASSWORD, NEW_PASSWORD);
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private UserDetailRequestMapper userDetailRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenChangePasswordWebRequestByGettingOldAndNewPasswordAsPairReturnPair() {
    
    when(validator.validate(CHANGE_PASSWORD_WEB_REQUEST)).thenReturn(
      CHANGE_PASSWORD_WEB_REQUEST);
    
    Pair<String, String> expectedOldAndNewPasswordPair = Pair.of(OLD_PASSWORD,
                                                                 NEW_PASSWORD
    );
    
    Pair<String, String> oldAndNewPasswordPair =
      userDetailRequestMapper.toOldAndNewPasswordPair(
        CHANGE_PASSWORD_WEB_REQUEST);
    
    assertThat(oldAndNewPasswordPair).isNotNull();
    assertThat(oldAndNewPasswordPair).isEqualTo(expectedOldAndNewPasswordPair);
    
    verify(validator).validate(CHANGE_PASSWORD_WEB_REQUEST);
  }
  
}