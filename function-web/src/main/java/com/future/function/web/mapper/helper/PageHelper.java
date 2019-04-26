package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.paging.Paging;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

  public static Pageable toPage(int page, int size, String sortBy, String sortType) {
    //TODO change hard code to fieldName.base entity.updatedAt
    sortBy = Optional.ofNullable(sortBy)
            .orElse("updatedAt");
    return new PageRequest(page - 1, size, toSort(sortBy, sortType));
  }

  private static Sort toSort(String sortBy, String sortType) {
    return Optional.ofNullable(sortType)
            .filter(val -> val.equalsIgnoreCase("desc"))
            .map(val -> Sort.Direction.DESC)
            .map(val -> new Sort(val, sortBy))
            .orElse(new Sort(Sort.DEFAULT_DIRECTION, sortBy));

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
