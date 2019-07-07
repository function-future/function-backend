package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ReportDetailScoreWebRequest;
import com.future.function.web.model.request.scoring.ScoreStudentWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReportDetailRequestMapper {

    private RequestValidator requestValidator;

    @Autowired
    public ReportDetailRequestMapper(RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    public List<ReportDetail> toReportDetailList(ReportDetailScoreWebRequest request, String reportId) {
        return Optional.ofNullable(request)
                .map(requestValidator::validate)
                .map(value -> toValidatedReportDetailList(value, reportId))
                .orElseThrow(() -> new BadRequestException("Failed mapping at #toReportDetailList"));
    }

    private List<ReportDetail> toValidatedReportDetailList(ReportDetailScoreWebRequest request, String reportId) {
        return request.getScores().stream()
                .map(this::buildReportDetail)
                .map(detail -> {
                    detail.setReport(Report.builder().id(reportId).build());
                    return detail;
                })
                .collect(Collectors.toList());
    }

    private ReportDetail buildReportDetail(ScoreStudentWebRequest request) {
        return ReportDetail.builder()
                .user(User.builder().id(request.getStudentId()).build())
                .point(request.getScore())
                .build();
    }
}
