package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
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

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<BatchWebResponse> getBatches(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    return BatchResponseMapper.toBatchesPagingResponse(
      batchService.getBatches(session, PageHelper.toPageable(page, size)));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<BatchWebResponse> createBatch(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestBody
      BatchWebRequest data
  ) {

    return BatchResponseMapper.toBatchDataResponse(
      HttpStatus.CREATED,
      batchService.createBatch(batchRequestMapper.toBatch(data))
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{batchId}")
  public DataResponse<BatchWebResponse> getBatch(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String batchId
  ) {

    return BatchResponseMapper.toBatchDataResponse(
      batchService.getBatchById(batchId));
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{batchId}")
  public DataResponse<BatchWebResponse> updateBatch(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String batchId,
    @RequestBody
      BatchWebRequest data
  ) {

    data.setId(batchId);
    return BatchResponseMapper.toBatchDataResponse(
      batchService.updateBatch(batchRequestMapper.toBatch(batchId, data)));
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{batchId}")
  public BaseResponse deleteBatch(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String batchId
  ) {

    batchService.deleteBatch(batchId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
