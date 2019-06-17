package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Menu;
import com.future.function.repository.feature.core.MenuRepository;
import com.future.function.service.api.feature.core.MenuService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
  
  private final MenuRepository menuRepository;
  
  public MenuServiceImpl(MenuRepository menuRepository) {
    
    this.menuRepository = menuRepository;
  }
  
  @Override
  public Map<String, Object> getSectionsByRole(Role role) {
    
    return Optional.ofNullable(role)
      .flatMap(menuRepository::findByRole)
      .map(Menu::getSections)
      .orElseGet(Collections::emptyMap);
  }
  
}
