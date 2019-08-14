package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchResponseMapper {

  public static DataResponse<BatchWebResponse> toBatchDataResponse(
    Batch batch
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toBatchWebResponse(batch));
  }

  public static DataResponse<BatchWebResponse> toBatchDataResponse(
    HttpStatus httpStatus, Batch batch
  ) {

    return ResponseHelper.toDataResponse(
      httpStatus, toBatchWebResponse(batch));
  }

  public static BatchWebResponse toBatchWebResponse(Batch batch) {

    return BatchWebResponse.builder()
      .id(batch.getId())
      .name(batch.getName())
      .code(batch.getCode())
      .build();
  }

  public static PagingResponse<BatchWebResponse> toBatchesPagingResponse(
    Page<Batch> data
  ) {

    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, toBatchWebResponseList(data), PageHelper.toPaging(data));
  }

  private static List<BatchWebResponse> toBatchWebResponseList(
    Page<Batch> data
  ) {

    return data.getContent()
      .stream()
      .map(BatchResponseMapper::toBatchWebResponse)
      .collect(Collectors.toList());
  }

}
