package com.future.function.service.impl.helper;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.model.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorizationHelper {
  
  public static final Role[] NON_STUDENT_ONLY = new Role[] {
    Role.ADMIN, Role.JUDGE, Role.MENTOR
  };
  
  public static final Role[] AUTHENTICATED_ONLY = new Role[] {
    Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT
  };
  
  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserEmail, T obj
  ) {
    
    return Optional.ofNullable(currentUserEmail)
      .filter(e -> e.equals(obj.getCreatedBy()))
      .map(ignored -> true)
      .orElseThrow(() -> new UnauthorizedException("Invalid User Email"));
  }
  
  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserEmail, Role currentUserRole, T obj, Role... allowedRoles
  ) {
    
    return Optional.ofNullable(currentUserEmail)
      .filter(email -> email.equals(obj.getCreatedBy()))
      .map(ignored -> true)
      .orElseGet(() -> AuthorizationHelper.isRoleValidForEdit(currentUserRole,
                                                              Arrays.asList(
                                                                allowedRoles)
      ));
  }
  
  private static <T extends BaseEntity> boolean isRoleValidForEdit(
    Role currentUserRole, List<Role> allowedRoles
  ) {
    
    return Optional.ofNullable(currentUserRole)
      .filter(allowedRoles::contains)
      .map(ignored -> true)
      .orElseThrow(() -> new UnauthorizedException("Invalid User Role"));
  }
  
}
