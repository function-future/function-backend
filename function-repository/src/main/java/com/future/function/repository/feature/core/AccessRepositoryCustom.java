package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;

import java.util.Optional;

public interface AccessRepositoryCustom {

  Optional<Access> findByUrlAndRole(String url, Role role);

}
