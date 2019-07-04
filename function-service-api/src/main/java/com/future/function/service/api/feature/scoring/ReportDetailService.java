package com.future.function.service.api.feature.scoring;

import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ReportDetailService {

    List<ReportDetail> findAllByReportId(String reportId);

    Report createReportDetailByReport(Report report, User student);

    Pair<ReportDetail, List<SummaryDTO>> findByStudentId(String studentId, String userId);

    Report giveScoreToEachStudentInDetail(String reportId, List<ReportDetail> detailList, String userId);

    void deleteAllByReportId(String reportId);

}
