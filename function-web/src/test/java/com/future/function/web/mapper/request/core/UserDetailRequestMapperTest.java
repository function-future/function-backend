package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import com.future.function.web.model.request.core.ChangeProfilePictureWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.util.Pair;

import java.util.Collections;

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

  private static final String EMAIL = "email";

  private static final String AVATAR_ID = "avatar-id";

  private static final ChangeProfilePictureWebRequest
    CHANGE_PROFILE_PICTURE_WEB_REQUEST = new ChangeProfilePictureWebRequest(
    Collections.singletonList(AVATAR_ID));

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

  @Test
  public void testGivenChangeProfilePictureWebRequestAndEmailByGettingUserFromRequestReturnUser() {

    when(validator.validate(CHANGE_PROFILE_PICTURE_WEB_REQUEST)).thenReturn(
      CHANGE_PROFILE_PICTURE_WEB_REQUEST);

    User expectedUser = User.builder()
      .email(EMAIL)
      .pictureV2(FileV2.builder()
                   .id(AVATAR_ID)
                   .build())
      .build();

    User user = userDetailRequestMapper.toUser(
      CHANGE_PROFILE_PICTURE_WEB_REQUEST, EMAIL);

    assertThat(user).isNotNull();
    assertThat(user).isEqualTo(expectedUser);

    verify(validator).validate(CHANGE_PROFILE_PICTURE_WEB_REQUEST);
  }

  @Test
  public void testGivenChangeProfilePictureWebRequestAndEmptyAvatarAndEmailByGettingUserFromRequestReturnUser() {

    ChangeProfilePictureWebRequest emptyAvatarRequest =
      new ChangeProfilePictureWebRequest(Collections.emptyList());
    when(validator.validate(emptyAvatarRequest)).thenReturn(emptyAvatarRequest);

    User expectedUser = User.builder()
      .email(EMAIL)
      .pictureV2(null)
      .build();

    User user = userDetailRequestMapper.toUser(emptyAvatarRequest, EMAIL);

    assertThat(user).isNotNull();
    assertThat(user).isEqualTo(expectedUser);

    verify(validator).validate(emptyAvatarRequest);
  }

}
