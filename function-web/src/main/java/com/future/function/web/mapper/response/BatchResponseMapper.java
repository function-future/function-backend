package com.future.function.web.mapper.response;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.web.model.base.DataResponse;
import com.future.function.web.model.base.PagingResponse;
import com.future.function.web.model.response.batch.BatchWebResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

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
  
  public static PagingResponse<Long> toBatchesPagingResponse(
    List<Batch> batches
  ) {
    
    return PagingResponse.<Long>builder().code(HttpStatus.OK.value())
      .status(HttpStatus.OK.getReasonPhrase())
      .data(batches.stream()
              .map(Batch::getNumber)
              .collect(Collectors.toList()))
      .build();
  }
  
}
