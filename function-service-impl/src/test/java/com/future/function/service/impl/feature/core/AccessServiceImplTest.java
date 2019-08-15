package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.repository.feature.core.AccessRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessServiceImplTest {

  private static final String ID = "id";

  private static final String URL_REGEX = "url-regex";

  private static final Map<String, Object> COMPONENTS =
    Collections.singletonMap("key", true);

  private static final Access ACCESS = new Access(
    ID, URL_REGEX, Role.MENTOR, COMPONENTS);

  private static final String URL = "url";

  @Mock
  private AccessRepository accessRepository;

  @InjectMocks
  private AccessServiceImpl accessService;

  @Before
  public void setUp() {}

  @After
  public void tearDown() {

    verifyNoMoreInteractions(accessRepository);
  }

  @Test
  public void testGivenRoleByGettingAccessComponentsByRoleReturnMap() {

    when(accessRepository.findByUrlAndRole(URL, Role.MENTOR)).thenReturn(
      Optional.of(ACCESS));

    Map<String, Object> foundMap = accessService.getComponentsByUrlAndRole(
      URL, Role.MENTOR);

    assertThat(foundMap).isNotEmpty();
    assertThat(foundMap).isEqualTo(COMPONENTS);

    verify(accessRepository).findByUrlAndRole(URL, Role.MENTOR);
  }

  @Test
  public void testGivenInvalidRoleByGettingAccessComponentsByRoleReturnEmptyMap() {

    Map<String, Object> foundMap = accessService.getComponentsByUrlAndRole(
      URL, null);

    assertThat(foundMap).isEmpty();

    verifyZeroInteractions(accessRepository);
  }

}
