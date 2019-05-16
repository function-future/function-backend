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

/**
 * Mapper class for batch web response.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchResponseMapper {
  
  /**
   * Converts a batch data to {@code BatchWebResponse} object, wrapped in
   * {@code DataResponse}.
   *
   * @param batch Batch data to be converted to response.
   *
   * @return {@code DataResponse<BatchWebResponse} - The converted batch data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.BatchWebResponse}
   */
  public static DataResponse<BatchWebResponse> toBatchDataResponse(
    Batch batch
  ) {
    
    return ResponseHelper.toDataResponse(
      HttpStatus.CREATED, buildBatchWebResponse(batch));
  }
  
  private static BatchWebResponse buildBatchWebResponse(Batch batch) {
    
    return BatchWebResponse.builder()
      .id(batch.getId())
      .name(batch.getName())
      .code(batch.getCode())
      .build();
  }
  
  /**
   * Converts batches data to {@code BatchWebResponse} object, wrapped in
   * {@code PagingResponse}.
   *
   * @param data Batches to be converted to response.
   *
   * @return {@code PagingResponse<BatchWebResponse>} - Batches' code found in
   * database, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse}.
   */
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
      .map(BatchResponseMapper::buildBatchWebResponse)
      .collect(Collectors.toList());
  }
  
}
