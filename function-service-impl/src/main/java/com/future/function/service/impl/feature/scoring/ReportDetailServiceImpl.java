package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<StudentSummaryVO> findAllSummaryByReportId(String reportId, String userId) {
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

    private List<StudentSummaryVO> getStudentsSummaryPoints(String userId, List<ReportDetail> list) {
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
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #createReportDetailByReport #ReportDetailService"));
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
                .orElseThrow(() -> new NotFoundException("Failed at #findByStudentId #ReportDetailService"));
    }

    private User checkUserEligibility(String studentId, User user) {
        if (user.getRole().equals(Role.STUDENT) && !user.getId().equals(studentId)) {
            throw new ForbiddenException("Faileld at #checkUserEligibility #ReportDetailService");
        } else {
            return user;
        }
    }

    @Override
    public List<ReportDetail> giveScoreToEachStudentInDetail(String reportId, List<ReportDetail> detailList) {
        return detailList.stream()
                .map(reportDetail -> findReportDetailAndValidateReportId(reportId, reportDetail))
                .map(reportDetailRepository::save)
                .collect(Collectors.toList());
    }

    private ReportDetail findReportDetailAndValidateReportId(String reportId, ReportDetail reportDetail) {
        return Optional.ofNullable(reportDetail)
                .map(ReportDetail::getUser)
                .map(User::getId)
                .flatMap(reportDetailRepository::findByUserId)
                .filter(detail -> detail.getReport().getId().equals(reportId))
                .map(detail -> {
                    CopyHelper.copyProperties(reportDetail, detail);
                    return detail;
                })
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #findReportDetailAndValidateReportId #ReportDetailService"));
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
