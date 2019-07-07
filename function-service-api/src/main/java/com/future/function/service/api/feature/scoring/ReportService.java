package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    Page<Report> findAllReport(String batchCode, String userId, Pageable pageable);

    Report findById(String id);

    Report createReport(Report report);

    Report updateReport(Report report);

    void deleteById(String id);

}
