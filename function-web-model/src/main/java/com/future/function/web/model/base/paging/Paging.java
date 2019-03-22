package com.future.function.web.model.base.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paging {

  private long currentPage;

  private long pageSize;

  private long totalPages;

  private long totalRecords;
}
