package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.repository.feature.core.AccessRepository;
import com.future.function.service.api.feature.core.AccessService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class AccessServiceImpl implements AccessService {

  private final AccessRepository accessRepository;

  public AccessServiceImpl(AccessRepository accessRepository) {

    this.accessRepository = accessRepository;
  }

  @Override
  public Map<String, Object> getComponentsByUrlAndRole(String url, Role role) {

    return Optional.ofNullable(role)
      .flatMap(r -> accessRepository.findByUrlAndRole(url, r))
      .map(Access::getComponents)
      .orElseGet(Collections::emptyMap);
  }

}
