package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
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
import org.springframework.data.domain.Pageable;
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
    Report report, String userId, String type, Pageable pageable
  ) {

    return Optional.ofNullable(report)
      .map(Report::getStudents)
      .map(list -> getStudentsSummaryPoints(userId, list, type, pageable))
      .orElseGet(ArrayList::new);
  }

  @Override
  public StudentSummaryVO findSummaryByStudentId(String studentId, String userId, String type, Pageable pageable) {
    return Optional.ofNullable(studentId)
            .flatMap(reportDetailRepository::findByUserIdAndDeletedFalse)
            .map(reportDetail -> this.findSummaryAndSetPoint(reportDetail, userId, type, pageable))
            .orElseThrow(() -> new NotFoundException("NOT_FOUND"));

  }

  private StudentSummaryVO findSummaryAndSetPoint(ReportDetail reportDetail, String userId, String type, Pageable pageable) {
    StudentSummaryVO summaryVO = summaryService.findAllPointSummaryByStudentId(reportDetail.getUser().getId(), pageable, userId, type);
    summaryVO.setPoint(reportDetail.getPoint());
    return summaryVO;
  }

  @Override
  public ReportDetail createReportDetailByReport(User student) {
    ReportDetail foundReportDetail = this.findByStudentId(student.getId(), student.getId());
    return Optional.ofNullable(student)
      .filter(ignored -> Objects.isNull(foundReportDetail))
      .map(this::buildReportDetail)
      .map(reportDetailRepository::save)
      .orElse(foundReportDetail);
  }

  private ReportDetail buildReportDetail(User student) {

    return ReportDetail.builder()
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
  public ReportDetail giveScoreToEachStudentInDetail(
    ReportDetail reportDetail
  ) {

    return Optional.ofNullable(reportDetail)
        .map(ReportDetail::getUser)
        .map(User::getId)
        .flatMap(reportDetailRepository::findByUserIdAndDeletedFalse)
        .map(
            currentReportDetail -> copyReportDetailRequestAttributes(reportDetail,
                currentReportDetail
            ))
        .map(reportDetailRepository::save)
        .orElseThrow(() -> new NotFoundException("NOT_FOUND"));
  }

  private ReportDetail copyReportDetailRequestAttributes(
    ReportDetail request, ReportDetail reportDetail
  ) {

    CopyHelper.copyProperties(request, reportDetail);
    return reportDetail;
  }

  @Override
  public void deleteAll() {

    Optional.ofNullable(reportDetailRepository.findAll())
      .ifPresent(this::deleteReportDetailList);
  }

  private void deleteReportDetailList(List<ReportDetail> list) {

    list.forEach(reportDetail -> {
      reportDetail.setDeleted(true);
      reportDetailRepository.save(reportDetail);
    });
  }

  private List<StudentSummaryVO> getStudentsSummaryPoints(
    String userId, List<ReportDetail> list, String type, Pageable pageable
  ) {

    return list.stream()
      .map(reportDetail -> getSummaryVOFromReportDetail(userId, reportDetail, type, pageable))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  private StudentSummaryVO getSummaryVOFromReportDetail(
    String userId, ReportDetail reportDetail, String type, Pageable pageable
  ) {

    return Optional.ofNullable(reportDetail)
      .map(ReportDetail::getUser)
      .map(User::getId)
      .map(studentId -> summaryService.findAllPointSummaryByStudentId(studentId,
                                                                      pageable, userId, type
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
