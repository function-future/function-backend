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
import org.springframework.data.domain.Pageable;
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
    public ReportServiceImpl(ReportRepository reportRepository, ReportDetailService reportDetailService,
                             UserService userService, BatchService batchService) {
        this.reportRepository = reportRepository;
        this.reportDetailService = reportDetailService;
        this.userService = userService;
        this.batchService = batchService;
    }

    @Override
    public Page<Report> findAllReport(String batchCode, Pageable pageable) {
      return Optional.ofNullable(batchCode)
          .map(batchService::getBatchByCode)
          .map(batch -> reportRepository.findAllByBatchAndDeletedFalse(batch, pageable))
          .map(this::mapReportPageToHaveStudentIds)
          .orElseGet(() -> PageHelper.empty(pageable));
    }

    private Page<Report> mapReportPageToHaveStudentIds(Page<Report> reportPage) {
        reportPage.getContent()
                .forEach(this::findStudentIdsByReportId);
        return reportPage;
    }

    @Override
    public Report findById(String id) {
        return Optional.ofNullable(id)
                .flatMap(reportRepository::findByIdAndDeletedFalse)
                .map(this::findStudentIdsByReportId)
                .orElseThrow(() -> new NotFoundException("Failed at #findById #ReportService"));
    }

    private Report findStudentIdsByReportId(Report report) {
        List<String> studentIds = reportDetailService.findAllDetailByReportId(report.getId())
                .stream()
                .map(ReportDetail::getUser)
                .map(User::getId)
                .collect(Collectors.toList());
        report.setStudentIds(studentIds);
        return report;
    }

    @Override
    public Report createReport(Report report) {
        return Optional.ofNullable(report)
                .map(this::setBatch)
                .map(currentReport -> this.setStudentIds(currentReport, null))
                .map(reportRepository::save)
                .map(currentReport -> createReportDetailByReportAndStudentId(currentReport, report.getStudentIds()))
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #createReport #ReportService"));
    }

  private Report setBatch(Report value) {
    Batch batch = batchService.getBatchByCode(value.getBatch().getCode());
    value.setBatch(batch);
    return value;
  }

  private Report setStudentIds(Report report, List<String> studentIds) {
      report.setStudentIds(studentIds);
      return report;
  }

  private Report createReportDetailByReportAndStudentId(Report report, List<String> studentIds) {
        return Optional.ofNullable(report)
                .map(currentReport -> createDetailsFromStudentIds(report, studentIds))
                .map(currentReport -> this.setStudentIds(currentReport, studentIds))
                .orElse(null);
    }

  private Report createDetailsFromStudentIds(Report report, List<String> studentIds) {
    return studentIds.stream()
            .map(userService::getUser)
            .map(student -> reportDetailService.createReportDetailByReport(report, student))
            .collect(Collectors.toList())
            .get(0);
  }

  @Override
    public Report updateReport(Report report) {
        return Optional.ofNullable(report)
                .map(this::checkStudentIdsChangedAndDeleteIfChanged)
                .map(Report::getId)
                .map(this::findById)
                .map(foundReport -> this.copyReportRequestAttributesIgnoreBatchField(report, foundReport))
                .map(foundReport -> this.setStudentIds(foundReport, null))
                .map(reportRepository::save)
                .map(foundReport -> this.setStudentIds(foundReport, report.getStudentIds()))
                .orElse(report);
    }

    private Report copyReportRequestAttributesIgnoreBatchField(Report request, Report report) {
      CopyHelper.copyProperties(request, report, FieldName.Report.BATCH);
      return report;
    }

    private Report checkStudentIdsChangedAndDeleteIfChanged(Report report) {
        return Optional.ofNullable(report)
                .filter(this::isStudentListChangedFromRepository)
                .orElseGet(() -> this.deleteAllDetailByReportId(report));
    }

    private boolean isStudentListChangedFromRepository(Report report) {
      return reportDetailService.findAllDetailByReportId(report.getId()).stream()
          .map(ReportDetail::getUser)
          .map(User::getId)
          .collect(Collectors.toList())
          .containsAll(report.getStudentIds());
    }

    private Report deleteAllDetailByReportId(Report report) {
        return Optional.ofNullable(report)
                .map(this::deleteExistingDetailAndCreateNew)
                .orElse(report);
    }

  private Report deleteExistingDetailAndCreateNew(Report currentReport) {
    reportDetailService.deleteAllByReportId(currentReport.getId());
    return this.createReportDetailByReportAndStudentId(currentReport, currentReport.getStudentIds());
  }

  @Override
    public void deleteById(String id) {
        Optional.ofNullable(id)
                .flatMap(reportRepository::findByIdAndDeletedFalse)
                .ifPresent(this::setDetailsAsDeletedAndSave);
    }

  @Override
  public List<StudentSummaryVO> findAllSummaryByReportId(String reportId, String userId) {
    return reportDetailService.findAllSummaryByReportId(reportId, userId);
  }

  @Override
  public List<ReportDetail> giveScoreToReportStudents(String reportId, List<ReportDetail> reportDetailList) {
    return Optional.ofNullable(reportId)
        .flatMap(reportRepository::findByIdAndDeletedFalse)
        .map(report -> reportDetailService.giveScoreToEachStudentInDetail(report, reportDetailList))
        .orElseThrow(() -> new UnsupportedOperationException("Failed at #giveScoreToReportStudents"));
  }

  private void setDetailsAsDeletedAndSave(Report report) {
    reportDetailService.deleteAllByReportId(report.getId());
    report.setDeleted(true);
    reportRepository.save(report);
  }
}
