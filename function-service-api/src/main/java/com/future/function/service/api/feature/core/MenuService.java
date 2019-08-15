package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;

import java.util.Map;

public interface MenuService {

  Map<String, Object> getSectionsByRole(Role role);

}
