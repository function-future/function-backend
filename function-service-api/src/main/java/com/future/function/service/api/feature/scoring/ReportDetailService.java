package com.future.function.service.api.feature.scoring;

import com.future.function.model.dto.scoring.StudentSummaryDTO;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;

import java.util.List;

public interface ReportDetailService {

  List<StudentSummaryDTO> findAllSummaryByReportId(String reportId, String userId);

  List<ReportDetail> findAllDetailByReportId(String reportId);

    Report createReportDetailByReport(Report report, User student);

  ReportDetail findByStudentId(String studentId, String userId);

    List<ReportDetail> giveScoreToEachStudentInDetail(String reportId, List<ReportDetail> detailList);

    void deleteAllByReportId(String reportId);

}
