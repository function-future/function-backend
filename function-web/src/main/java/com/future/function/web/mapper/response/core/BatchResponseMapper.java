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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchResponseMapper {

  public static DataResponse<BatchWebResponse> toBatchDataResponse(
    Batch batch
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toBatchWebResponse(batch));
  }

  public static BatchWebResponse toBatchWebResponse(Batch batch) {

    return BatchWebResponse.builder()
      .id(batch.getId())
      .name(batch.getName())
      .code(batch.getCode())
      .build();
  }

  public static DataResponse<BatchWebResponse> toBatchDataResponse(
    HttpStatus httpStatus, Batch batch
  ) {

    return ResponseHelper.toDataResponse(httpStatus, toBatchWebResponse(batch));
  }

  public static DataResponse<List<BatchWebResponse>> toBatchesDataResponse(
    List<Batch> batches
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toBatchWebResponseList(batches));
  }

  private static List<BatchWebResponse> toBatchWebResponseList(
    List<Batch> batches
  ) {

    return batches.stream()
      .map(BatchResponseMapper::toBatchWebResponse)
      .collect(Collectors.toList());
  }

}
