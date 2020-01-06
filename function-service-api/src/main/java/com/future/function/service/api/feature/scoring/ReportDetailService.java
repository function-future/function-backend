package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;

import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReportDetailService {

  List<StudentSummaryVO> findAllSummaryByReportId(
    Report report, String userId, String type, Pageable pageable
  );

  StudentSummaryVO findSummaryByStudentId(String studentId, String userId, String type, Pageable pageable);

  ReportDetail createOrGetReportDetail(User student);

  ReportDetail findByStudentId(String studentId, String userId);

  ReportDetail giveScoreToEachStudentInDetail(ReportDetail reportDetail);

  void deleteAll();

}
