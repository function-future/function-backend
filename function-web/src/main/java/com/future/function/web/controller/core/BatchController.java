package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.mapper.request.core.BatchRequestMapper;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.request.core.BatchWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  
  private final BatchRequestMapper batchRequestMapper;
  
  @Autowired
  public BatchController(
    BatchService batchService, BatchRequestMapper batchRequestMapper
  ) {
    
    this.batchService = batchService;
    this.batchRequestMapper = batchRequestMapper;
  }
  
  /**
   * Retrieves list of batches' code in database.
   *
   * @return {@code DataResponse<List<Long>>} - Batches' code found in
   * database, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse}.
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public DataResponse<List<Long>> getBatches() {
    
    return BatchResponseMapper.toBatchesDataResponse(batchService.getBatches());
  }
  
  /**
   * Saves a new batch to database.
   *
   * @return {@code DataResponse<BatchWebResponse} - The created batch data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.BatchWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<BatchWebResponse> createBatch(
    @RequestBody
      BatchWebRequest data
  ) {
    
    return BatchResponseMapper.toBatchDataResponse(
      batchService.createBatch(batchRequestMapper.toBatch(data)));
  }
  
}
