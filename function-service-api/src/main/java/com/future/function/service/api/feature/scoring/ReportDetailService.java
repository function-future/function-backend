package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;

import java.util.List;

public interface ReportDetailService {

    List<StudentSummaryVO> findAllSummaryByReportId(String reportId, String userId);

    List<ReportDetail> findAllDetailByReportId(String reportId);

    Report createReportDetailByReport(Report report, User student);

    ReportDetail findByStudentId(String studentId, String userId);

    List<ReportDetail> giveScoreToEachStudentInDetail(Report report, List<ReportDetail> detailList);

    void deleteReportDetailByStudentId(String studentId);

    void deleteAllByReportId(String reportId);

}
