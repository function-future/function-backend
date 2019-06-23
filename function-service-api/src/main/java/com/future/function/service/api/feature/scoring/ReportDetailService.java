package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;

import java.util.List;

public interface ReportDetailService {

    List<ReportDetail> findAllByReportId(String reportId);

    Report createReportDetailByReport(Report report, User student);

    void deleteAllByReportId(String reportId);

}
