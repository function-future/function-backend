package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.repository.feature.scoring.ReportRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.ReportService;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportDetailService reportDetailService;

    @Autowired
    private UserService userService;

    @Override
    public Page<Report> findAllReport(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    @Override
    public Report findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(reportRepository::findByIdAndDeletedFalse)
                .orElseThrow(() -> new NotFoundException("Report not found"));
    }

    @Override
    public Report createReport(Report report) {
        return Optional.ofNullable(report)
                .map(Report::getStudentIds)
                .map(studentIds -> createReportDetailByReportAndStudentId(report, studentIds))
                .map(reportRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Failed to create report"));
    }

    private Report createReportDetailByReportAndStudentId(Report report, List<String> studentIds) {
        return studentIds.stream()
                .map(userService::getUser)
                .map(student -> reportDetailService.createReportDetailByReport(report, student))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Report updateReport(Report report) {

        Optional.ofNullable(report)
                .map(this::checkStudentIdsChangedAndDeleteIfChanged)
                .map(Report::getId)
                .flatMap(reportRepository::findByIdAndDeletedFalse)
                .map(foundReport -> {
                    CopyHelper.copyProperties(report, foundReport);
                    return foundReport;
                })
                .map(reportRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Failed to update report"));
        return null;
    }

    private Report checkStudentIdsChangedAndDeleteIfChanged(Report report) {
        List<String> foundStudentIds = reportDetailService.findAllByReportId(report.getId()).stream()
                .map(ReportDetail::getUser)
                .map(User::getId)
                .collect(Collectors.toList());
        return Optional.ofNullable(report)
                .map(Report::getStudentIds)
                .filter(list -> Objects.nonNull(list) && !list.isEmpty())
                .filter(foundStudentIds::containsAll)
                .map(value -> report)
                .orElseGet(() -> deleteAllDetailByReportId(report));
    }

    private Report deleteAllDetailByReportId(Report report) {
        reportDetailService.deleteAllByReportId(report.getId());
        return report;
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(id)
                .flatMap(reportRepository::findByIdAndDeletedFalse)
                .ifPresent(report -> {
                    report.setDeleted(true);
                    reportRepository.save(report);
                });
    }
}
