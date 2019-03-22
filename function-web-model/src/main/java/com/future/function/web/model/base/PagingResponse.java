package com.future.function.web.model.base;

import com.future.function.web.model.base.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PagingResponse<T> extends DataResponse<T> {

  private Paging paging;
}
