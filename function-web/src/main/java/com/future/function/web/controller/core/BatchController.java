package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for batch APIs.
 */
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
  public DataResponse<List<Long>> getBatches() {
  
    return BatchResponseMapper.toBatchesDataResponse(batchService.getBatches());
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<BatchWebResponse> createBatch() {
    
    return BatchResponseMapper.toBatchDataResponse(batchService.createBatch());
  }
  
}
