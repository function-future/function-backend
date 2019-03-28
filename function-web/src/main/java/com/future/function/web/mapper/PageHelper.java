package com.future.function.web.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.future.function.web.model.base.paging.Paging;

public class PageHelper {

  public static Pageable toPage(int page, int size) {

    return PageRequest.of(page - 1, size);
  }

  public static <T> Paging toPaging(Page<T> data) {

    return Paging.builder()
        .currentPage(data.getNumber())
        .pageSize(data.getNumberOfElements())
        .totalPages(data.getTotalPages())
        .totalRecords(data.getTotalElements())
        .build();
  }

}
