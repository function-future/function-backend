package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.vo.scoring.StudentSummaryVO;
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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportResponseMapper {

  public static DataResponse<ReportWebResponse> toDataReportWebResponse(Report report) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildReportWebResponse(report));
  }

  private static List<UserWebResponse> mapStudentsToWebResponse(List<User> students) {
    return students.stream()
        .map(UserResponseMapper::toUserDataResponse)
        .map(DataResponse::getData)
        .collect(Collectors.toList());
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
            .id(value.getId())
            .name(value.getTitle())
            .description(value.getDescription())
            .batchCode(value.getBatch().getCode())
            .studentCount(value.getStudents().size())
            .students(mapStudentsToWebResponse(value.getStudents()))
            .uploadedDate(value.getCreatedAt())
            .build())
        .orElseThrow(() -> new UnsupportedOperationException("Failed at #buildReportWebResponse #ReportResponseMapper"));
  }

  private static List<ReportWebResponse> buildReportWebResponseList(Page<Report> reportPage) {
    return reportPage.getContent()
        .stream()
        .map(ReportResponseMapper::buildReportWebResponse)
        .collect(Collectors.toList());
  }

}
