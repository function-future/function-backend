package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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
   * @return {@code DataResponse<BatchWebResponse} - The converted batch data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.BatchWebResponse}
   */
  public static DataResponse<BatchWebResponse> toBatchDataResponse(
          Batch batch
  ) {

    return DataResponse.<BatchWebResponse>builder().code(
            HttpStatus.CREATED.value())
            .status(ResponseHelper.toProperStatusFormat(
                    HttpStatus.CREATED.getReasonPhrase()))
            .data(new BatchWebResponse(batch.getNumber()))
            .build();
  }

  /**
   * Converts batches data to {@code List<Long>} object, wrapped in
   * {@code DataResponse}.
   *
   * @param batches Batches to be converted to response.
   * @return {@code DataResponse<List<Long>>} - Batches' number found in
   * database, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse}.
   */
  public static DataResponse<List<Long>> toBatchesDataResponse(
          List<Batch> batches
  ) {

    return DataResponse.<List<Long>>builder().code(HttpStatus.OK.value())
            .status(HttpStatus.OK.getReasonPhrase())
            .data(batches.stream()
                    .map(Batch::getNumber)
                    .collect(Collectors.toList()))
            .build();
  }

}
