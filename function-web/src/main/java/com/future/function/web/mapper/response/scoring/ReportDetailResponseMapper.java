package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportDetailResponseMapper {

    public static DataResponse<ReportDetailWebResponse> toDataReportDetailWebResponse(StudentSummaryVO summaryDTO, String urlPrefix) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildReportDetailWebResponse(summaryDTO, urlPrefix));
    }

    public static DataResponse<List<ReportDetailWebResponse>> toDataListReportDetailWebResponseFromReportDetail(
            HttpStatus httpStatus, List<ReportDetail> reportDetailList, String urlPrefix) {
        return ResponseHelper.toDataResponse(httpStatus, buildListFromReportDetailList(reportDetailList, urlPrefix));
    }

    private static ReportDetailWebResponse buildReportDetailWebResponse(StudentSummaryVO summaryDTO, String urlPrefix) {
        return ReportDetailWebResponse.builder()
                .studentId(summaryDTO.getStudentId())
            .studentName(summaryDTO.getStudentName())
            .batchCode(summaryDTO.getBatchCode())
            .university(summaryDTO.getUniversity())
                .avatar(urlPrefix.concat(summaryDTO.getAvatar()))
            .scores(ScoreSummaryResponseMapper.toDataListSummaryResponse(summaryDTO.getScores()).getData())
                .point(summaryDTO.getPoint())
                .build();
    }

    private static List<ReportDetailWebResponse> buildListOfSummaryDTOs(List<StudentSummaryVO> summaryDTOs, String urlPrefix) {
    return summaryDTOs.stream()
        .map(summaryDTO -> ReportDetailResponseMapper.buildReportDetailWebResponse(summaryDTO, urlPrefix))
                .collect(Collectors.toList());
    }

    private static ReportDetailWebResponse buildReportDetailWebResponse(ReportDetail reportDetail, String urlPrefix) {
        return ReportDetailWebResponse.builder()
                .studentId(reportDetail.getUser().getId())
                .studentName(reportDetail.getUser().getName())
                .batchCode(reportDetail.getUser().getBatch().getCode())
                .university(reportDetail.getUser().getUniversity())
                .avatar(getNullableUserAvatar(reportDetail.getUser(), urlPrefix))
                .point(reportDetail.getPoint())
                .build();
    }

    private static String getNullableUserAvatar(User user, String urlPrefix) {
        return Optional.ofNullable(user)
            .map(User::getPictureV2)
            .map(FileV2::getFileUrl)
            .map(urlPrefix::concat)
            .orElse(null);
    }

    private static List<ReportDetailWebResponse> buildListFromReportDetailList(List<ReportDetail> reportDetails, String urlPrefix) {
        return reportDetails.stream()
                .map(reportDetail -> ReportDetailResponseMapper.buildReportDetailWebResponse(reportDetail, urlPrefix))
                .collect(Collectors.toList());
    }

    public static DataResponse<List<ReportDetailWebResponse>> toDataListReportDetailWebResponse(List<StudentSummaryVO> summaryDTOs, String urlPrefix) {
      return ResponseHelper.toDataResponse(HttpStatus.OK, buildListOfSummaryDTOs(summaryDTOs, urlPrefix));
    }

}
