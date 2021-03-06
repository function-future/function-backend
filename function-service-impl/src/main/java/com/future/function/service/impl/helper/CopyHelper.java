package com.future.function.service.impl.helper;

import com.future.function.model.util.constant.FieldName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CopyHelper {

  private static final String[] IGNORED_PROPERTIES = new String[] {
    FieldName.BaseEntity.ID, FieldName.BaseEntity.CREATED_AT,
    FieldName.BaseEntity.CREATED_BY, FieldName.BaseEntity.VERSION,
    FieldName.User.class.getSimpleName().toLowerCase()
  };

  public static <T> void copyProperties(T source, T target) {

    BeanUtils.copyProperties(source, target, IGNORED_PROPERTIES);
  }

  public static <T> void copyProperties(
    T source, T target, String... ignoredProperties
  ) {

    BeanUtils.copyProperties(
      source, target, ArrayUtils.addAll(IGNORED_PROPERTIES, ignoredProperties));
  }

}
