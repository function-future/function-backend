package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class AccessRepositoryTest {

  private static final String URL_REGEX = "\\/[0-9A-Za-z]+(\\-[0-9A-Za-z]+)*$";

  @Autowired
  private AccessRepository accessRepository;

  @Before
  public void setUp() {

    Access access = Access.builder()
      .urlRegex(URL_REGEX)
      .role(Role.STUDENT)
      .build();
    accessRepository.save(access);
  }

  @After
  public void tearDown() {

    accessRepository.deleteAll();
  }

  @Test
  public void testGivenUrlAndRoleByFindingAccessByUrlAndRoleReturnAccess() {

    String url = "/test";

    assertThat(
      accessRepository.findByUrlAndRole(url, Role.STUDENT)).isNotEqualTo(
      Optional.empty());
  }

}
