package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.paging.Paging;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageHelper {

  public static Pageable toPageable(int page, int size) {

    return new PageRequest(page - 1, size);
  }

  public static <T> Paging toPaging(Page<T> data) {

    return Paging.builder()
      .page(data.getNumber() + 1L)
      .size(data.getSize())
      .totalRecords(data.getTotalElements())
      .build();
  }

}
