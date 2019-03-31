package com.future.function.web.controller;

import com.future.function.service.api.feature.batch.BatchService;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.BatchResponseMapper;
import com.future.function.web.model.base.BaseResponse;
import com.future.function.web.model.base.DataResponse;
import com.future.function.web.model.base.PagingResponse;
import com.future.function.web.model.response.batch.BatchWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/core/batches")
public class BatchController {
  
  private final BatchService batchService;
  
  @Autowired
  public BatchController(BatchService batchService) {
    
    this.batchService = batchService;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<Long> getBatches() {
    
    return BatchResponseMapper.toBatchesPagingResponse(
      batchService.getBatches());
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<BatchWebResponse> createBatch() {
    
    return BatchResponseMapper.toBatchDataResponse(batchService.createBatch());
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{batchNumber}")
  public BaseResponse deleteBatch(
    @PathVariable
      long batchNumber
  ) {
    
    batchService.deleteBatch(batchNumber);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
