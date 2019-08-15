package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.repository.feature.scoring.ReportDetailRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportDetailServiceImpl implements ReportDetailService {

  private ReportDetailRepository reportDetailRepository;

  private UserService userService;

  private SummaryService summaryService;

  @Autowired
  public ReportDetailServiceImpl(
    ReportDetailRepository reportDetailRepository, UserService userService,
    SummaryService summaryService
  ) {

    this.reportDetailRepository = reportDetailRepository;
    this.userService = userService;
    this.summaryService = summaryService;
  }

  @Override
  public List<StudentSummaryVO> findAllSummaryByReportId(
    String reportId, String userId
  ) {

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

  @Override
  public Report createReportDetailByReport(Report report, User student) {

    return Optional.ofNullable(report)
      .filter(ignored -> Objects.isNull(
        this.findByStudentId(student.getId(), student.getId())))
      .map(value -> buildReportDetail(value, student))
      .map(reportDetailRepository::save)
      .map(ReportDetail::getReport)
      .orElseThrow(() -> new UnsupportedOperationException("ComparisonExists"));
  }

  private ReportDetail buildReportDetail(Report report, User student) {

    return ReportDetail.builder()
      .report(report)
      .user(student)
      .build();
  }

  @Override
  public ReportDetail findByStudentId(String studentId, String userId) {

    return Optional.ofNullable(userId)
      .map(userService::getUser)
      .filter(
        user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentId,
                                                              AuthorizationHelper.getScoringAllowedRoles()
        ))
      .flatMap(
        user -> reportDetailRepository.findByUserIdAndDeletedFalse(studentId))
      .orElse(null);
  }

  @Override
  public List<ReportDetail> giveScoreToEachStudentInDetail(
    Report report, List<ReportDetail> detailList
  ) {

    return detailList.stream()
      .map(reportDetail -> findReportDetailAndMapReport(report, reportDetail))
      .map(reportDetailRepository::save)
      .collect(Collectors.toList());
  }

  private ReportDetail findReportDetailAndMapReport(
    Report report, ReportDetail reportDetail
  ) {

    return Optional.ofNullable(reportDetail)
      .map(ReportDetail::getUser)
      .map(User::getId)
      .flatMap(reportDetailRepository::findByUserIdAndDeletedFalse)
      .map(
        currentReportDetail -> copyReportDetailRequestAttributes(reportDetail,
                                                                 currentReportDetail
        ))
      .map(currentReportDetail -> setReportOfReportDetail(report,
                                                          currentReportDetail
      ))
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #findReportDetailAndMapReport #ReportDetailService"));
  }

  private ReportDetail setReportOfReportDetail(
    Report report, ReportDetail currentReportDetail
  ) {

    currentReportDetail.setReport(report);
    return currentReportDetail;
  }

  private ReportDetail copyReportDetailRequestAttributes(
    ReportDetail request, ReportDetail reportDetail
  ) {

    CopyHelper.copyProperties(request, reportDetail);
    return reportDetail;
  }

  @Override
  public void deleteAllByReportId(String reportId) {

    Optional.ofNullable(reportId)
      .map(this::findAllDetailByReportId)
      .ifPresent(this::deleteReportDetailList);
  }

  private void deleteReportDetailList(List<ReportDetail> list) {

    list.forEach(reportDetail -> {
      reportDetail.setDeleted(true);
      reportDetailRepository.save(reportDetail);
    });
  }

  private List<StudentSummaryVO> getStudentsSummaryPoints(
    String userId, List<ReportDetail> list
  ) {

    return list.stream()
      .map(reportDetail -> getSummaryVOFromReportDetail(userId, reportDetail))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  private StudentSummaryVO getSummaryVOFromReportDetail(
    String userId, ReportDetail reportDetail
  ) {

    return Optional.ofNullable(reportDetail)
      .map(ReportDetail::getUser)
      .map(User::getId)
      .map(studentId -> summaryService.findAllPointSummaryByStudentId(studentId,
                                                                      userId
      ))
      .map(summary -> setSummaryPoint(reportDetail, summary))
      .orElse(null);
  }

  private StudentSummaryVO setSummaryPoint(
    ReportDetail reportDetail, StudentSummaryVO summary
  ) {

    summary.setPoint(reportDetail.getPoint());
    return summary;
  }

}
