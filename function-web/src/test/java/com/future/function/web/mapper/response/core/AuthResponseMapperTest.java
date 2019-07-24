package com.future.function.web.mapper.response.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.AuthWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthResponseMapperTest {

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testGivenUserByMappingToDataResponseReturnDataResponseObject() {

    User user = User.builder()
      .id("id")
      .role(Role.MENTOR)
      .email("email")
      .name("name")
      .phone("phone")
      .address("address")
      .pictureV2(FileV2.builder()
                   .fileUrl("/file-url")
                   .build())
      .build();
  
    String urlPrefix = "url-prefix";

    AuthWebResponse authWebResponse = AuthWebResponse.builder()
      .id("id")
      .name("name")
      .email("email")
      .avatar(urlPrefix + "/file-url")
      .role(Role.MENTOR.name())
      .build();

    DataResponse<AuthWebResponse> expectedDataResponse =
      DataResponse.<AuthWebResponse>builder().code(200)
        .status("OK")
        .data(authWebResponse)
        .build();

    DataResponse<AuthWebResponse> dataResponse =
      AuthResponseMapper.toAuthDataResponse(user, urlPrefix);

    assertThat(dataResponse).isEqualTo(expectedDataResponse);
  }

}
