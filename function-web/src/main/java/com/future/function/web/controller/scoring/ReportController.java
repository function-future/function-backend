package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.ReportService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/judgings")
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
      @RequestParam(defaultValue = "1", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size,
      Session session) {
    return ReportResponseMapper.toPagingReportWebResponse(reportService.findAllReport(PageHelper.toPageable(page, size)));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReportWebResponse> findOne(@PathVariable String id) {
    return ReportResponseMapper.toDataReportWebResponse(reportService.findById(id));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReportWebResponse> createFinalJudging(@RequestBody ReportWebRequest request) {
    return ReportResponseMapper.toDataReportWebResponse(HttpStatus.CREATED,
        reportService.createReport(requestMapper.toReport(request)));
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ReportWebResponse> updateFinalJudging(@PathVariable String id, @RequestBody ReportWebRequest request) {
    return ReportResponseMapper.toDataReportWebResponse(reportService.updateReport(requestMapper.toReport(request, id)));
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteById(@PathVariable String id) {
    reportService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
