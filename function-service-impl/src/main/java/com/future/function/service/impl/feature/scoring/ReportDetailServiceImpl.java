package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.dto.scoring.StudentSummaryDTO;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.service.impl.helper.CopyHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportDetailServiceImpl implements ReportDetailService {

    private ReportDetailRepository reportDetailRepository;

    private UserService userService;

    private SummaryService summaryService;

    @Autowired
    public ReportDetailServiceImpl(ReportDetailRepository reportDetailRepository, UserService userService,
        SummaryService summaryService) {
        this.reportDetailRepository = reportDetailRepository;
        this.userService = userService;
        this.summaryService = summaryService;
    }

    @Override
    public List<StudentSummaryDTO> findAllSummaryByReportId(String reportId, String userId) {
        return Optional.ofNullable(reportId)
            .map(reportDetailRepository::findAllByReportIdAndDeletedFalse)
            .map(list -> getStudentsSummaryPoints(userId, list))
            .orElseGet(ArrayList::new);
    }

    @Override
    public List<ReportDetail> findAllDetailByReportId(String reportId) {
        return Optional.ofNullable(reportId)
            .map(reportDetailRepository::findAllByReportIdAndDeletedFalse)
            .orElseGet(ArrayList::new);
    }

    private List<StudentSummaryDTO> getStudentsSummaryPoints(String userId, List<ReportDetail> list) {
        return list.stream()
            .map(ReportDetail::getUser)
            .map(User::getId)
            .map(studentId -> summaryService.findAllPointSummaryByStudentId(studentId, userId))
            .collect(Collectors.toList());
    }

    @Override
    public Report createReportDetailByReport(Report report, User student) {
        return Optional.ofNullable(report)
                .map(value -> buildReportDetail(report, student))
                .map(reportDetailRepository::save)
                .map(ReportDetail::getReport)
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #createReportDetailByReport"));
    }

    private ReportDetail buildReportDetail(Report report, User student) {
        return ReportDetail.builder().report(report).user(student).build();
    }

    @Override
    public ReportDetail findByStudentId(String studentId, String userId) {
        return Optional.ofNullable(userId)
                .map(userService::getUser)
                .map(user -> this.checkUserEligibility(studentId, user))
                .flatMap(user -> reportDetailRepository.findByUserId(studentId))
                .orElseThrow(() -> new NotFoundException("Report not found"));
    }

    private User checkUserEligibility(String studentId, User user) {
        if (user.getRole().equals(Role.STUDENT) && !user.getId().equals(studentId)) {
            throw new ForbiddenException("User not allowed");
        } else {
            return user;
        }
    }

    @Override
    public Report giveScoreToEachStudentInDetail(String reportId, List<ReportDetail> detailList, String userId) {
        return detailList.stream()
                .map(reportDetail -> findReportDetailAndValidateReportId(reportId, userId, reportDetail))
                .map(reportDetailRepository::save)
                .findFirst()
                .map(ReportDetail::getReport)
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #giveScoreToEachStudentInDetail"));
    }

    private ReportDetail findReportDetailAndValidateReportId(String reportId, String userId, ReportDetail reportDetail) {
        ReportDetail foundReportDetail = this.findByStudentId(reportDetail.getUser().getId(), userId);
        if (foundReportDetail.getReport().getId().equals(reportId)) {
            CopyHelper.copyProperties(reportDetail, foundReportDetail);
            return foundReportDetail;
        } else {
            throw new UnsupportedOperationException("Report id not equal");
        }
    }

    @Override
    public void deleteAllByReportId(String reportId) {
        Optional.ofNullable(reportId)
            .map(this::findAllDetailByReportId)
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
