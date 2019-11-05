package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ReportService {

  Page<Report> findAllReport(String batchCode, Pageable pageable);

  Report findById(String id);

  Report createReport(Report report);

  Report updateReport(Report report);

  void deleteById(String id);

  List<StudentSummaryVO> findAllSummaryByReportId(
    String reportId, String userId, String type, Pageable pageable
  );

  Page<Pair<User, Integer>> findAllStudentsAndFinalPointByBatch(
    String batchCode, Pageable pageable
  );

}
