package com.future.function.service.impl.helper;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.base.BaseEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationHelperTest {
  
  private static final String CREATED_BY = "created-by";
  
  private static final String UPDATED_BY = "updated-by";
  
  private static final BaseEntity BASE_ENTITY = new BaseEntity(
    0L, CREATED_BY, 1L, UPDATED_BY, false, 0L);
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenEmailAndEntityAndEqualEmailAndEntityCreatorByCheckingIfUserWithEmailIsAuthorizedForEditReturnTrue() {
    
    assertThat(AuthorizationHelper.isAuthorizedForEdit(CREATED_BY,
                                                       BASE_ENTITY
    )).isTrue();
  }
  
  @Test
  public void testGivenEmailAndEntityAndInequalEmailAndEntityCreatorByCheckingIfUserWithEmailIsAuthorizedForEditReturnForbiddenException() {
    
    catchException(
      () -> AuthorizationHelper.isAuthorizedForEdit("", BASE_ENTITY));
    
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid User Email");
  }
  
  @Test
  public void testGivenEmailAndRoleAndEntityAndAllowedRolesAndRoleMatchingAnyAllowedRolesReturnTrue() {
    
    assertThat(
      AuthorizationHelper.isAuthorizedForEdit("", Role.STUDENT, BASE_ENTITY,
                                              AuthorizationHelper.AUTHENTICATED_ONLY
      )).isTrue();
  }
  
  @Test
  public void testGivenEmailAndRoleAndEntityAndSomeAllowedRolesAndRoleMatchingAnyAllowedRolesReturnTrue() {
    
    assertThat(
      AuthorizationHelper.isAuthorizedForEdit("", Role.STUDENT, BASE_ENTITY,
                                              Role.STUDENT, Role.MENTOR
      )).isTrue();
  }
  
  @Test
  public void testGivenEmailAndRoleAndEntityAndAllowedRolesAndNotMatchingAnyAllowedRolesReturnForbiddenException() {
    
    catchException(
      () -> AuthorizationHelper.isAuthorizedForEdit("", Role.STUDENT,
                                                    BASE_ENTITY,
                                                    AuthorizationHelper.NON_STUDENT_ONLY
      ));
    
    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Invalid User Role");
  }
  
  @Test
  public void testGivenRoleAndAllowedRolesReturnBoolean() {
  
    assertThat(
      AuthorizationHelper.isRoleValidForEdit(Role.ADMIN, Role.ADMIN)).isTrue();
    assertThat(AuthorizationHelper.isRoleValidForEdit(Role.ADMIN)).isFalse();
  }
  
}
