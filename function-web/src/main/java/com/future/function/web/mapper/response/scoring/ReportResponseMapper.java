package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportResponseMapper {

  public static DataResponse<ReportWebResponse> toDataReportWebResponse(Report report) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildReportWebResponse(report));
  }

  public static DataResponse<ReportWebResponse> toDataReportWebResponse(HttpStatus httpStatus, Report report) {
    return ResponseHelper.toDataResponse(httpStatus, buildReportWebResponse(report));
  }

  public static PagingResponse<ReportWebResponse> toPagingReportWebResponse(Page<Report> reportPage) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, buildReportWebResponseList(reportPage),
        PageHelper.toPaging(reportPage));
  }

  private static ReportWebResponse buildReportWebResponse(Report report) {
    return Optional.ofNullable(report)
        .map(value -> ReportWebResponse
            .builder()
            .title(report.getTitle())
            .description(value.getDescription())
            .usedAt(getLongFormatOfDate(value))
            .studentCount(value.getStudentIds().size())
            .build())
        .orElseThrow(() -> new UnsupportedOperationException("Response Mapping failed"));
  }

  private static long getLongFormatOfDate(Report value) {
    return value.getUsedAt().atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond();
  }

  private static List<ReportWebResponse> buildReportWebResponseList(Page<Report> reportPage) {
    return reportPage.getContent()
        .stream()
        .map(ReportResponseMapper::buildReportWebResponse)
        .collect(Collectors.toList());
  }

}
