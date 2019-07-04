package com.future.function.web.mapper.response.scoring;

import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.ReportDetailWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportDetailResponseMapper {

    public static DataResponse<ReportDetailWebResponse> toDataReportDetailWebResponse(Pair<ReportDetail, List<SummaryDTO>> pair) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildReportDetailWebResponse(pair.getFirst(), pair.getSecond()));
    }

    private static ReportDetailWebResponse buildReportDetailWebResponse(ReportDetail reportDetail, List<SummaryDTO> dtoList) {
        return ReportDetailWebResponse.builder()
                .id(reportDetail.getId())
                .studentName(reportDetail.getUser().getName())
                .batchCode(reportDetail.getUser().getBatch().getCode())
                .scores(ScoreSummaryResponseMapper.toDataListSummaryResponse(dtoList).getData())
                .build();
    }

    private static List<ReportDetailWebResponse> buildListReportDetailWebResponse(List<Pair<ReportDetail, List<SummaryDTO>>> pairs) {
        return pairs.stream()
                .map(pair -> buildReportDetailWebResponse(pair.getFirst(), pair.getSecond()))
                .collect(Collectors.toList());
    }

    public static DataResponse<List<ReportDetailWebResponse>> toDataListReportDetailWebResponse(List<Pair<ReportDetail, List<SummaryDTO>>> pairs) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildListReportDetailWebResponse(pairs));
    }

}
