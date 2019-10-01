package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import com.future.function.web.model.response.feature.scoring.ReportWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportResponseMapper {

  public static DataResponse<ReportWebResponse> toDataReportWebResponse(
    Report report, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(HttpStatus.OK,
                                         buildReportWebResponse(report,
                                                                urlPrefix
                                         )
    );
  }

  private static ReportWebResponse buildReportWebResponse(
    Report report, String urlPrefix
  ) {

    return Optional.ofNullable(report)
      .map(value -> ReportWebResponse.builder()
        .id(value.getId())
        .name(value.getTitle())
        .description(value.getDescription())
        .batchCode(value.getBatch()
                     .getCode())
        .studentCount(value.getStudents()
                        .size())
        .students(toUserWebResponse(value.getStudents(), urlPrefix))
        .uploadedDate(value.getCreatedAt())
        .build())
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #buildReportWebResponse #ReportResponseMapper"));
  }

  private static List<UserWebResponse> toUserWebResponse(List<ReportDetail> students, String urlPrefix) {
    return UserResponseMapper.toUserWebResponseList(students.stream()
        .map(ReportDetail::getUser)
        .collect(Collectors.toList()), urlPrefix);

  }

  public static DataResponse<ReportWebResponse> toDataReportWebResponse(
    HttpStatus httpStatus, Report report, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(httpStatus,
                                         buildReportWebResponse(report,
                                                                urlPrefix
                                         )
    );
  }

  public static PagingResponse<ReportWebResponse> toPagingReportWebResponse(
    Page<Report> reportPage, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           buildReportWebResponseList(
                                             reportPage, urlPrefix),
                                           PageHelper.toPaging(reportPage)
    );
  }

  private static List<ReportWebResponse> buildReportWebResponseList(
    Page<Report> reportPage, String urlPrefix
  ) {

    return reportPage.getContent()
      .stream()
      .map(report -> ReportResponseMapper.buildReportWebResponse(report,
                                                                 urlPrefix
      ))
      .collect(Collectors.toList());
  }

}
