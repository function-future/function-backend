package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
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
    public Page<Report> findAllReport(String userId, Pageable pageable) {
        User user = userService.getUser(userId);
        if (user.getRole().equals(Role.ADMIN)) {
            return reportRepository.findAll(pageable);
        } else {
            return reportRepository.findAllByUsedAtEqualsAndDeletedFalse(LocalDate.now(ZoneId.systemDefault()), pageable);
        }
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
                .map(value -> {
                    value.setStudentIds(null);
                    return value;
                })
                .orElse(null);
    }

    @Override
    public Report updateReport(Report report) {
        return Optional.ofNullable(report)
                .map(this::checkStudentIdsChangedAndDeleteIfChanged)
                .map(Report::getId)
                .map(this::findById)
                .map(foundReport -> {
                    CopyHelper.copyProperties(report, foundReport);
                    foundReport.setStudentIds(null);
                    return foundReport;
                })
                .map(reportRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Failed to update report"));
    }

    private Report checkStudentIdsChangedAndDeleteIfChanged(Report report) {
        List<String> foundStudentIds = reportDetailService.findAllByReportId(report.getId()).stream()
                .map(ReportDetail::getUser)
                .map(User::getId)
                .collect(Collectors.toList());
        return Optional.ofNullable(report)
                .map(Report::getStudentIds)
                .map(this::validateList)
                .filter(foundStudentIds::containsAll)
                .map(value -> report)
                .orElseGet(() -> deleteAllDetailByReportId(report));
    }

    private List<String> validateList(List<String> list) {
        if (list.isEmpty())
            throw new UnsupportedOperationException("Update failed: student list empty");
        return list;
    }

    private Report deleteAllDetailByReportId(Report report) {
        return Optional.ofNullable(report)
                .map(Report::getId)
                .map(reportId -> {
                    reportDetailService.deleteAllByReportId(reportId);
                    return this.createReportDetailByReportAndStudentId(report, report.getStudentIds());
                })
                .orElseThrow(() -> new UnsupportedOperationException("Update failed at #deleteAllReportByReportId"));
    }

    @Override
    public Report giveScoreToEachStudent(String reportId, List<ReportDetail> reportDetailList, String userId) {
        return Optional.ofNullable(reportId)
                .map(this::findById)
                .map(Report::getId)
                .map(id -> reportDetailService.giveScoreToEachStudentInDetail(id, reportDetailList, userId))
                .orElseThrow(() -> new UnsupportedOperationException("give score failed at #giveScoreToEachStudent"));
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(id)
                .flatMap(reportRepository::findByIdAndDeletedFalse)
                .ifPresent(report -> {
                    reportDetailService.deleteAllByReportId(report.getId());
                    report.setDeleted(true);
                    reportRepository.save(report);
                });
    }
}
