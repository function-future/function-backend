package com.future.function.service.impl.helper;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
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
    String currentUserId, T obj
  ) {

    return AuthorizationHelper.isUserCreatorOfObject(currentUserId, obj)
      .orElseThrow(() -> new ForbiddenException("Invalid User Email"));
  }

  private static <T extends BaseEntity> Optional<Boolean> isUserCreatorOfObject(
    String currentUserId, T obj
  ) {

    return Optional.ofNullable(currentUserId)
      .filter(e -> e.equals(obj.getCreatedBy()))
      .map(ignored -> true);
  }

  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserId, Role currentUserRole, T obj, Role... allowedRoles
  ) {

    return AuthorizationHelper.isAuthorizedForEdit(currentUserId,
                                                   currentUserRole, obj,
                                                   Arrays.asList(allowedRoles)
    );
  }

  public static <T extends BaseEntity> boolean isAuthorizedForEdit(
    String currentUserId, Role currentUserRole, T obj,
    List<Role> allowedRoles
  ) {

    return AuthorizationHelper.isUserCreatorOfObject(currentUserId, obj)
      .orElseGet(() -> AuthorizationHelper.isRoleValidForEdit(currentUserRole,
                                                              allowedRoles
      ));
  }

  public static boolean isUserAuthorizedForAccess(User currentUser, String id, Role... allowedRoles) {
    return Optional.ofNullable(currentUser)
            .filter(user -> Role.STUDENT.equals(user.getRole()))
            .map(user -> user.getId().equals(id))
            .map(returnValue -> {
              if (!returnValue) throw new ForbiddenException("User not Allowed");
              return true;
            })
            .orElseGet(() -> isRoleValidForEdit(currentUser.getRole(), Arrays.asList(allowedRoles)));
  }

  public static boolean isRoleValidForEdit(
    Role currentUserRole, Role... allowedRoles
  ) {

    List<Role> roles = Arrays.asList(allowedRoles);

    return Optional.ofNullable(currentUserRole)
      .filter(roles::contains)
      .isPresent();
  }

  private static boolean isRoleValidForEdit(
    Role currentUserRole, List<Role> allowedRoles
  ) {

    return Optional.ofNullable(currentUserRole)
      .filter(allowedRoles::contains)
      .map(ignored -> true)
      .orElseThrow(() -> new ForbiddenException("Invalid User Role"));
  }

  public static Role[] getScoringAllowedRoles() {
    return Arrays.asList(Role.ADMIN, Role.JUDGE, Role.MENTOR).toArray(new Role[0]);
  }

}
