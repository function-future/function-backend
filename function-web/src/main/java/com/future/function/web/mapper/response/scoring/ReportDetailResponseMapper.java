package com.future.function.web.mapper.response.scoring;

import com.future.function.model.dto.scoring.StudentSummaryDTO;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportDetailResponseMapper {

  public static DataResponse<ReportDetailWebResponse> toDataReportDetailWebResponse(StudentSummaryDTO summaryDTO) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildReportDetailWebResponse(summaryDTO));
    }

  private static ReportDetailWebResponse buildReportDetailWebResponse(StudentSummaryDTO summaryDTO) {
        return ReportDetailWebResponse.builder()
            .studentName(summaryDTO.getStudentName())
            .batchCode(summaryDTO.getBatchCode())
            .university(summaryDTO.getUniversity())
            .scores(ScoreSummaryResponseMapper.toDataListSummaryResponse(summaryDTO.getScores()).getData())
                .build();
    }

  private static List<ReportDetailWebResponse> buildListReportDetailWebResponse(List<StudentSummaryDTO> summaryDTOs) {
    return summaryDTOs.stream()
        .map(ReportDetailResponseMapper::buildReportDetailWebResponse)
                .collect(Collectors.toList());
    }

  public static DataResponse<List<ReportDetailWebResponse>> toDataListReportDetailWebResponse(List<StudentSummaryDTO> summaryDTOs) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildListReportDetailWebResponse(summaryDTOs));
    }

}
