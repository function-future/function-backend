package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;

import java.util.Map;

public interface AccessService {

  Map<String, Object> getComponentsByUrlAndRole(String url, Role role);

}
