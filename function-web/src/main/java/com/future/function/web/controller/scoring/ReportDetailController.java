package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.request.scoring.ReportDetailRequestMapper;
import com.future.function.web.mapper.response.scoring.ReportDetailResponseMapper;
import com.future.function.web.model.request.scoring.ReportDetailScoreWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/final-judgings/{judgingId}/comparison")
public class ReportDetailController {

    private ReportDetailService reportDetailService;

    private ReportDetailRequestMapper requestMapper;

    @Autowired
    public ReportDetailController(ReportDetailService reportDetailService, ReportDetailRequestMapper requestMapper) {
        this.reportDetailService = reportDetailService;
        this.requestMapper = requestMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR})
    public DataResponse<List<ReportDetailWebResponse>> findComparisonByReportId(@PathVariable("judgingId") String judgingId,
                                                                                Session session) {
        return ReportDetailResponseMapper.toDataListReportDetailWebResponse(
                reportDetailService.findAllSummaryByReportId(judgingId, session.getUserId()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @WithAnyRole(roles = Role.JUDGE)
    public DataResponse<List<ReportDetailWebResponse>> giveFinalScoreToStudentsByReportId(@PathVariable("judgingId") String judgingId,
                                                                                          @RequestBody ReportDetailScoreWebRequest request,
                                                                                          Session session) {
        return ReportDetailResponseMapper.toDataListReportDetailWebResponseFromReportDetail(
                HttpStatus.CREATED,
                reportDetailService.giveScoreToEachStudentInDetail(
                        judgingId,
                        requestMapper.toReportDetailList(request, judgingId)));
    }

}
