package com.future.function.service.impl.helper;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.model.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorizationHelper {
  
  private static final List<Role> NON_STUDENT_ROLES = Arrays.asList(
    Role.ADMIN, Role.JUDGE, Role.MENTOR);
  
  public static final List<Role> NON_STUDENT_ONLY =
    Collections.unmodifiableList(NON_STUDENT_ROLES);
  
  private static final List<Role> AUTHENTICATED_ROLES = Arrays.asList(
    Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT);
  
  public static final List<Role> AUTHENTICATED_ONLY =
    Collections.unmodifiableList(AUTHENTICATED_ROLES);
  
  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserEmail, T obj
  ) {
    
    return AuthorizationHelper.isUserCreatorOfObject(currentUserEmail, obj)
      .orElseThrow(() -> new UnauthorizedException("Invalid User Email"));
  }
  
  private static <T extends BaseEntity> Optional<Boolean> isUserCreatorOfObject(
    String currentUserEmail, T obj
  ) {
    
    return Optional.ofNullable(currentUserEmail)
      .filter(e -> e.equals(obj.getCreatedBy()))
      .map(ignored -> true);
  }
  
  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserEmail, Role currentUserRole, T obj, Role... allowedRoles
  ) {
    
    return AuthorizationHelper.isAuthorizedForEdit(currentUserEmail,
                                                   currentUserRole, obj,
                                                   Arrays.asList(allowedRoles)
    );
  }
  
  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserEmail, Role currentUserRole, T obj,
    List<Role> allowedRoles
  ) {
    
    return AuthorizationHelper.isUserCreatorOfObject(currentUserEmail, obj)
      .orElseGet(() -> AuthorizationHelper.isRoleValidForEdit(currentUserRole,
                                                              allowedRoles
      ));
  }
  
  private static boolean isRoleValidForEdit(
    Role currentUserRole, List<Role> allowedRoles
  ) {
    
    return Optional.ofNullable(currentUserRole)
      .filter(allowedRoles::contains)
      .map(ignored -> true)
      .orElseThrow(() -> new UnauthorizedException("Invalid User Role"));
  }
  
}
