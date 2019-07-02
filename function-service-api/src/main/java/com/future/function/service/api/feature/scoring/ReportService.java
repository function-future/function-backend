package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {

    Page<Report> findAllReport(String userId, Pageable pageable);

    Report findById(String id);

    Report createReport(Report report);

    Report updateReport(Report report);

    Report giveScoreToEachStudent(String reportId, List<ReportDetail> reportDetailList, String userId);

    void deleteById(String id);

}
