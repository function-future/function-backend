package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
      HttpStatus.CREATED, new BatchWebResponse(batch.getNumber()));
  }
  
  /**
   * Converts batches data to {@code List<Long>} object, wrapped in
   * {@code DataResponse}.
   *
   * @param batches Batches to be converted to response.
   *
   * @return {@code DataResponse<List<Long>>} - Batches' number found in
   * database, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse}.
   */
  public static DataResponse<List<Long>> toBatchesDataResponse(
    List<Batch> batches
  ) {
  
    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toBatchNumberList(batches));
  }
  
  private static List<Long> toBatchNumberList(List<Batch> batches) {
    
    return batches.stream()
      .map(Batch::getNumber)
      .collect(Collectors.toList());
  }
  
}
