package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReportRequestMapper {

  private RequestValidator validator;

  @Autowired
  public ReportRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public Report toReport(ReportWebRequest request, String batchCode) {

    return toValidatedReport(request, batchCode);
  }

  private Report toValidatedReport(ReportWebRequest request, String batchCode) {

    return Optional.ofNullable(request)
      .map(validator::validate)
      .map(reportWebRequest -> Report.builder()
        .title(reportWebRequest.getName())
        .description(reportWebRequest.getDescription())
        .batch(Batch.builder()
                 .code(batchCode)
                 .build())
        .students(buildStudentsFromStudentIds(reportWebRequest.getStudents()))
        .build())
      .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private List<ReportDetail> buildStudentsFromStudentIds(List<String> studentIds) {

    return studentIds.stream()
      .map(id -> User.builder()
        .id(id)
        .build())
      .map(user -> ReportDetail.builder().user(user).build())
      .collect(Collectors.toList());
  }

  public Report toReport(
    ReportWebRequest request, String id, String batchCode
  ) {

    Report report = toValidatedReport(request, batchCode);
    report.setId(id);
    return report;
  }

}
