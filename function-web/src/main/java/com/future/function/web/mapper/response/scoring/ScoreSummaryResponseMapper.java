package com.future.function.web.mapper.response.scoring;

import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.SummaryResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreSummaryResponseMapper {

    public static DataResponse<SummaryResponse> toDataSummaryResponse(SummaryDTO summaryDTO) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildSummaryResponse(summaryDTO));
    }

    private static SummaryResponse buildSummaryResponse(SummaryDTO summaryDTO) {
        return SummaryResponse.builder()
                .title(summaryDTO.getTitle())
                .type(summaryDTO.getType())
                .point(summaryDTO.getPoint())
                .build();
    }

    private static List<SummaryResponse> buildSummaryResponseList(List<SummaryDTO> dtoList) {
        return dtoList.stream()
                .map(ScoreSummaryResponseMapper::buildSummaryResponse)
                .collect(Collectors.toList());
    }

    public static DataResponse<List<SummaryResponse>> toDataListSummaryResponse(List<SummaryDTO> summaryDTOList) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildSummaryResponseList(summaryDTOList));
    }

}
