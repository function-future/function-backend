package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Batch;
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
    
    return DataResponse.<BatchWebResponse>builder().code(
      HttpStatus.CREATED.value())
      .status(HttpStatus.CREATED.getReasonPhrase())
      .data(new BatchWebResponse(batch.getNumber()))
      .build();
  }
  
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
