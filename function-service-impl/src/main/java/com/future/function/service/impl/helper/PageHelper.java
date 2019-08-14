package com.future.function.service.impl.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageHelper {
  
  public static <T> Page<T> empty(Pageable pageable) {
    
    return PageHelper.toPage(Collections.emptyList(), pageable);
  }
  
  public static <T> Page<T> toPage(List<T> data, Pageable pageable) {
    
    return new PageImpl<>(data, pageable, data.size());
  }
  
}
