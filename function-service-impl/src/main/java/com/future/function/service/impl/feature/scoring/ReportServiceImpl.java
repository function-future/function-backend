package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.util.constant.FieldName;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.repository.feature.scoring.ReportRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.ReportService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

  private ReportRepository reportRepository;

  private ReportDetailService reportDetailService;

  private UserService userService;

  private BatchService batchService;

  @Autowired
  public ReportServiceImpl(
    ReportRepository reportRepository, ReportDetailService reportDetailService,
    UserService userService, BatchService batchService
  ) {

    this.reportRepository = reportRepository;
    this.reportDetailService = reportDetailService;
    this.userService = userService;
    this.batchService = batchService;
  }

  @Override
  public Page<Report> findAllReport(String batchCode, Pageable pageable) {

    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode)
      .map(batch -> reportRepository.findAllByBatchAndDeletedFalse(batch,
                                                                   pageable
      ))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public Report findById(String id) {

    return Optional.ofNullable(id)
      .flatMap(reportRepository::findByIdAndDeletedFalse)
      .orElseThrow(
        () -> new NotFoundException("Failed at #findById #ReportService"));
  }

  @Override
  public Report createReport(Report report) {

    return Optional.ofNullable(report)
      .map(this::setBatch)
      .map(currentReport -> createReportDetailByReportAndStudentId(currentReport, currentReport.getStudents()))
      .map(reportRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #createReport #ReportService"));
  }

  private Report setBatch(Report value) {

    Batch batch = batchService.getBatchByCode(value.getBatch()
                                                .getCode());
    value.setBatch(batch);
    return value;
  }

  private Report createReportDetailByReportAndStudentId(
    Report report, List<ReportDetail> students
  ) {

    return Optional.ofNullable(students)
      .map(this::createOrGetDetails)
      .map(studentList -> this.setReportDetails(report, studentList))
      .filter(currentReport -> !reportRepository.existsByStudentsContainsAndDeletedFalse(report.getStudents()))
      .orElse(null);
  }

  private Report setReportDetails(Report report, List<ReportDetail> students) {
    report.setStudents(students);
    return report;
  }

  private List<ReportDetail> createOrGetDetails(
    List<ReportDetail> students
  ) {

    return students.stream()
      .map(ReportDetail::getUser)
      .map(User::getId)
      .map(userService::getUser)
      .map(student -> reportDetailService.createOrGetReportDetail(student))
      .collect(Collectors.toList());
  }

  @Override
  public Report updateReport(Report report) {

    List<ReportDetail> students = report.getStudents();
    return Optional.ofNullable(report)
      .map(Report::getId)
      .map(this::findById)
      .map(foundReport -> this.copyReportRequestAttributesIgnoreBatchField(report, foundReport))
      .map(currentReport -> this.checkStudentIdsChangedAndDeleteIfChanged(currentReport, students))
      .map(reportRepository::save)
      .orElse(report);
  }

  private Report copyReportRequestAttributesIgnoreBatchField(
    Report request, Report report
  ) {

    CopyHelper.copyProperties(request, report, FieldName.Report.BATCH, FieldName.Report.STUDENTS);
    return report;
  }

  private Report checkStudentIdsChangedAndDeleteIfChanged(Report report, List<ReportDetail> students) {

    return Optional.ofNullable(report)
      .filter(currentReport -> this.isStudentListChangedFromRepository(currentReport, students))
      .orElseGet(() -> this.createReportDetailByReportAndStudentId(report, report.getStudents()));
  }

  private boolean isStudentListChangedFromRepository(Report report, List<ReportDetail> students) {

    List<String> studentIds = students
        .stream()
        .map(ReportDetail::getUser)
        .map(User::getId)
        .collect(Collectors.toList());
    return Optional.ofNullable(report)
        .map(currentReport -> isStudentListEquals(studentIds, currentReport.getStudents()))
        .orElse(false);
  }

  private boolean isStudentListEquals(List<String> students, List<ReportDetail> foundStudents) {
    return foundStudents.stream().map(ReportDetail::getUser).map(User::getId).collect(Collectors.toList()).containsAll(students);
  }

  @Override
  public void deleteById(String id) {

    Optional.ofNullable(id)
      .flatMap(reportRepository::findByIdAndDeletedFalse)
      .ifPresent(this::setDetailsAsDeletedAndSave);
  }

  @Override
  public List<StudentSummaryVO> findAllSummaryByReportId(
    String reportId, String userId, String type, Pageable pageable
  ) {
    Report report = this.findById(reportId);
    return reportDetailService.findAllSummaryByReportId(report, userId, type, pageable);
  }

  @Override
  public Page<Pair<User, Integer>> findAllStudentsAndFinalPointByBatch(
    String batchCode, Pageable pageable
  ) {

    Page<User> userPage = userService.getStudentsWithinBatch(
      batchCode, pageable);
    return userPage.getContent()
      .stream()
      .map(this::findFinalPointAndMapToPair)
      .collect(Collectors.collectingAndThen(Collectors.toList(),
                                            pairList -> new PageImpl<>(pairList,
                                                                       pageable,
                                                                       userPage.getTotalElements()
                                            )
      ));
  }

  private Pair<User, Integer> findFinalPointAndMapToPair(User user) {

    Integer totalPoint = getFinalPointFromNullableReportDetail(
      reportDetailService.findByStudentId(user.getId(), user.getId()));
    return Pair.of(user, totalPoint);
  }

  private Integer getFinalPointFromNullableReportDetail(
    ReportDetail reportDetail
  ) {

    return Optional.ofNullable(reportDetail)
      .map(ReportDetail::getPoint)
      .orElse(0);
  }

  private void setDetailsAsDeletedAndSave(Report report) {

    report.setDeleted(true);
    reportRepository.save(report);
  }

}
