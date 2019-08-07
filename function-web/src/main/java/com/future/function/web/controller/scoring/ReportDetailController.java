package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.ReportService;
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
@RequestMapping("/api/scoring/batches/{batchCode}/judgings/{judgingId}/comparisons")
public class ReportDetailController {

    private ReportService reportService;
    private ReportDetailRequestMapper requestMapper;
    private FileProperties fileProperties;

    @Autowired
    public ReportDetailController(ReportService reportService, ReportDetailRequestMapper requestMapper,
        FileProperties fileProperties) {
        this.reportService = reportService;
        this.requestMapper = requestMapper;
        this.fileProperties = fileProperties;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<List<ReportDetailWebResponse>> findComparisonByReportId(@PathVariable String judgingId,
        @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR}) Session session) {
        return ReportDetailResponseMapper.toDataListReportDetailWebResponse(
            reportService.findAllSummaryByReportId(judgingId, session.getUserId()), fileProperties.getUrlPrefix());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<List<ReportDetailWebResponse>> giveFinalScoreToStudentsByReportId(@PathVariable String judgingId,
                                                                                          @RequestBody ReportDetailScoreWebRequest request,
                                                                                          @WithAnyRole(roles = Role.JUDGE) Session session) {
        return ReportDetailResponseMapper.toDataListReportDetailWebResponseFromReportDetail(
                HttpStatus.CREATED,
            reportService.giveScoreToReportStudents(
                        judgingId,
                        requestMapper.toReportDetailList(request, judgingId)),
            fileProperties.getUrlPrefix());
    }

}
