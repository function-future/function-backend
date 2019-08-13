package com.future.function.web.mapper.response.core.embedded;

import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorWebResponseMapperTest {

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testGivenUserByMappingToAuthorWebResponseReturnAuthorWebResponse() {

    String id = "id";
    String name = "name";

    User user = User.builder()
      .id(id)
      .name(name)
      .build();

    AuthorWebResponse expectedAuthorWebResponse = AuthorWebResponse.builder()
      .id(id)
      .name(name)
      .build();

    AuthorWebResponse authorWebResponse =
      AuthorWebResponseMapper.buildAuthorWebResponse(user);

    assertThat(authorWebResponse).isNotNull();
    assertThat(authorWebResponse).isEqualTo(expectedAuthorWebResponse);
  }

}
