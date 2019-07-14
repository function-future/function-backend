package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.response.scoring.ReportDetailResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/summary/{studentId}")
public class SummaryController {

  private SummaryService summaryService;

  @Autowired
  public SummaryController(SummaryService summaryService) {
    this.summaryService = summaryService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
  public DataResponse<ReportDetailWebResponse> findAllSummaryByStudentId(@PathVariable String studentId,
                                                                         Session session) {
    return ReportDetailResponseMapper.toDataReportDetailWebResponse(
        summaryService.findAllPointSummaryByStudentId(studentId, session.getUserId()));
  }

}
