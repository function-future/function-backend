package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Menu;
import com.future.function.repository.feature.core.MenuRepository;
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
public class MenuServiceImplTest {
  
  private static final String ID = "id";
  
  private static final Map<String, Object> SECTIONS = Collections.singletonMap(
    "key", true);
  
  private static final Menu MENU = new Menu(ID, Role.MENTOR, SECTIONS);
  
  @Mock
  private MenuRepository menuRepository;
  
  @InjectMocks
  private MenuServiceImpl menuService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(menuRepository);
  }
  
  @Test
  public void testGivenRoleByGettingMenuSectionsByRoleReturnMap() {
    
    when(menuRepository.findByRole(Role.MENTOR)).thenReturn(Optional.of(MENU));
    
    Map<String, Object> foundMap = menuService.getSectionsByRole(Role.MENTOR);
    
    assertThat(foundMap).isNotEmpty();
    assertThat(foundMap).isEqualTo(SECTIONS);
    
    verify(menuRepository).findByRole(Role.MENTOR);
  }
  
  @Test
  public void testGivenInvalidRoleByGettingMenuSectionsByRoleReturnEmptyMap() {
    
    Map<String, Object> foundMap = menuService.getSectionsByRole(null);
    
    assertThat(foundMap).isEmpty();
    
    verifyZeroInteractions(menuRepository);
  }
  
}