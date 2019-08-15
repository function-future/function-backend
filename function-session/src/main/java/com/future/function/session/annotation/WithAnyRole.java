package com.future.function.session.annotation;

import com.future.function.common.enumeration.core.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
public @interface WithAnyRole {

  Role[] roles() default {};

  boolean noUnauthorized() default false;

}
