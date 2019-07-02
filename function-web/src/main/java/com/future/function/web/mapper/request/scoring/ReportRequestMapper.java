package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ReportWebRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ReportRequestMapper {

  private RequestValidator validator;

  public Report toReport(ReportWebRequest request) {
    return toValidatedReport(request);
  }

  public Report toReport(ReportWebRequest request, String id) {
    Report report = toValidatedReport(request);
    report.setId(id);
    return report;
  }

  private Report toValidatedReport(ReportWebRequest request) {
    return Optional.ofNullable(request)
        .map(validator::validate)
        .map(value -> Report.builder()
            .title(value.getName())
            .description(value.getDescription())
            .usedAt(getLocalDate(value))
            .studentIds(value.getStudents())
            .build())
        .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  private LocalDate getLocalDate(ReportWebRequest value) {
    return Instant.ofEpochSecond(value.getUsedAt()).atZone(ZoneId.systemDefault()).toLocalDate();
  }

}
