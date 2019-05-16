package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.BatchService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.BatchRequestMapper;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.request.core.BatchWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public PagingResponse<BatchWebResponse> getBatches(
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {
    
    return BatchResponseMapper.toBatchesPagingResponse(
      batchService.getBatches(PageHelper.toPageable(page, size)));
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
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{batchId}")
  public DataResponse<BatchWebResponse> getBatch(
    @PathVariable
      String batchId
  ) {
    
    return BatchResponseMapper.toBatchDataResponse(
      batchService.getBatchById(batchId));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{batchId}")
  public DataResponse<BatchWebResponse> updateBatch(
    @PathVariable
      String batchId,
    @RequestBody
      BatchWebRequest data
  ) {
    
    return BatchResponseMapper.toBatchDataResponse(
      batchService.updateBatch(batchRequestMapper.toBatch(batchId, data)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{batchId}")
  public BaseResponse deleteBatch(
    @PathVariable
      String batchId
  ) {
    
    batchService.deleteBatch(batchId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
