package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportDetailServiceImpl implements ReportDetailService {

    @Autowired
    private ReportDetailRepository reportDetailRepository;

    @Autowired
    private UserService userService;

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
                .orElseThrow(() -> new UnsupportedOperationException("Map Report Detail Scores failed"));
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
