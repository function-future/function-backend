package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.paging.Paging;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Helper class for creating paging-related objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageHelper {

  /**
   * Creates {@link org.springframework.data.domain.Pageable} object.
   *
   * @param page Currently read page.
   * @param size Size of data in a page.
   * @return {@code Pageable} - Pageable object in form of
   * {@link org.springframework.data.domain.PageRequest} as an implementation
   * of {@code Pageable} interface.
   */
  public static Pageable toPage(int page, int size) {

    return new PageRequest(page - 1, size);
  }

  /**
   * Constructs paging response given paged data.
   *
   * @param data Paged data, containing all data and page-related info.
   * @param <T>  Type of class of the specified data.
   * @return {@code Paging} - Paging object containing important information
   * that will be exposed to end-users.
   */
  public static <T> Paging toPaging(Page<T> data) {

    return Paging.builder()
            .currentPage(data.getNumber())
            .pageSize(data.getSize())
            .totalPages(data.getTotalPages())
            .totalRecords(data.getTotalElements())
            .build();
  }

}
