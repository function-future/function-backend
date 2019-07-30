package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.ReportService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.ReportRequestMapper;
import com.future.function.web.mapper.response.scoring.ReportResponseMapper;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/judgings")
public class ReportController {

  private ReportService reportService;
  private ReportRequestMapper requestMapper;

  @Autowired
  public ReportController(ReportService reportService, ReportRequestMapper requestMapper) {
    this.reportService = reportService;
    this.requestMapper = requestMapper;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<ReportWebResponse> findAllReportByUsedAtNow(
          @PathVariable("batchCode") String batchCode,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @WithAnyRole(roles = {Role.ADMIN, Role.MENTOR, Role.JUDGE}) Session session) {
    return ReportResponseMapper.toPagingReportWebResponse(reportService
            .findAllReport(batchCode, PageHelper.toPageable(page, size)));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReportWebResponse> findOne(@PathVariable String id,
      @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR}) Session session) {
    return ReportResponseMapper.toDataReportWebResponse(reportService.findById(id));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReportWebResponse> createFinalJudging(@PathVariable String batchCode,
      @RequestBody ReportWebRequest request, @WithAnyRole(roles = Role.ADMIN) Session session) {
    return ReportResponseMapper.toDataReportWebResponse(HttpStatus.CREATED,
        reportService.createReport(requestMapper.toReport(request, batchCode)));
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReportWebResponse> updateFinalJudging(@PathVariable String batchCode, @PathVariable String id,
                                                            @RequestBody ReportWebRequest request,
                                                            @WithAnyRole(roles = Role.ADMIN) Session session) {
    return ReportResponseMapper.toDataReportWebResponse(reportService.updateReport(requestMapper.toReport(request, id, batchCode)));
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteById(@PathVariable String id, @WithAnyRole(roles = Role.ADMIN) Session session) {
    reportService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
