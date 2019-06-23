package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportDetailServiceImpl implements ReportDetailService {

    @Autowired
    private ReportDetailRepository reportDetailRepository;

    @Override
    public List<ReportDetail> findAllByReportId(String reportId) {
        return Optional.ofNullable(reportId)
                .map(reportDetailRepository::findAllByReportIdAndDeletedFalse)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Report createReportDetailByReport(Report report, User student) {
        return null;
    }

    @Override
    public void deleteAllByReportId(String reportId) {
        Optional.ofNullable(reportId)
                .map(this::findAllByReportId)
                .ifPresent(this::deleteReportDetailList);
    }

    private void deleteReportDetailList(List<ReportDetail> list) {
        list
                .forEach(reportDetail -> {
                    reportDetail.setDeleted(true);
                    reportDetailRepository.save(reportDetail);
                });
    }
}
