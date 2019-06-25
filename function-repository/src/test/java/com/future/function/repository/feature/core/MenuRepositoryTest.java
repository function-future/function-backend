package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Menu;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class MenuRepositoryTest {
  
  @Autowired
  private MenuRepository menuRepository;
  
  @Before
  public void setUp() {
    
    Menu menu = Menu.builder()
      .role(Role.JUDGE)
      .sections(Collections.singletonMap("key", true))
      .build();
    menuRepository.save(menu);
  }
  
  @After
  public void tearDown() {
    
    menuRepository.deleteAll();
  }
  
  @Test
  public void testGivenRoleByFindingMenuByRoleReturnMenu() {
    
    assertThat(menuRepository.findByRole(Role.JUDGE)).isNotEqualTo(
      Optional.empty());
  }
  
}