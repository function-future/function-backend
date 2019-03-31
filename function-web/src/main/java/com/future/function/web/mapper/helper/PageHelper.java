package com.future.function.web.mapper.helper;

import com.future.function.web.model.base.paging.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageHelper {
  
  public static Pageable toPage(int page, int size) {
    
    return new PageRequest(page - 1, size);
  }
  
  public static <T> Paging toPaging(Page<T> data) {
    
    return Paging.builder()
      .currentPage(data.getNumber())
      .pageSize(data.getSize())
      .totalPages(data.getTotalPages())
      .totalRecords(data.getTotalElements())
      .build();
  }
  
}
