package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

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

  public Report toReport(ReportWebRequest request, String id, String batchCode) {
    Report report = toValidatedReport(request, batchCode);
    report.setId(id);
    return report;
  }

  private Report toValidatedReport(ReportWebRequest request, String batchCode) {
    return Optional.ofNullable(request)
        .map(validator::validate)
        .map(value -> Report.builder()
            .title(value.getName())
            .description(value.getDescription())
            .batch(Batch.builder().code(batchCode).build())
            .usedAt(getLocalDate(value))
            .studentIds(value.getStudents())
            .build())
        .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private LocalDate getLocalDate(ReportWebRequest value) {
    return Instant.ofEpochSecond(value.getUsedAt()).atZone(ZoneId.systemDefault()).toLocalDate();
  }

}
