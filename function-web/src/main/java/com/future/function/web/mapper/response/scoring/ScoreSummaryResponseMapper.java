package com.future.function.web.mapper.response.scoring;

import com.future.function.model.dto.scoring.SummaryDTO;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.SummaryWebResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreSummaryResponseMapper {

  public static DataResponse<SummaryWebResponse> toDataSummaryResponse(SummaryDTO summaryDTO) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildSummaryResponse(summaryDTO));
    }

  private static SummaryWebResponse buildSummaryResponse(SummaryDTO summaryDTO) {
    return SummaryWebResponse.builder()
                .title(summaryDTO.getTitle())
                .type(summaryDTO.getType())
                .point(summaryDTO.getPoint())
                .build();
    }

  private static List<SummaryWebResponse> buildSummaryResponseList(List<SummaryDTO> dtoList) {
        return dtoList.stream()
                .map(ScoreSummaryResponseMapper::buildSummaryResponse)
                .collect(Collectors.toList());
    }

  public static DataResponse<List<SummaryWebResponse>> toDataListSummaryResponse(List<SummaryDTO> summaryDTOList) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, buildSummaryResponseList(summaryDTOList));
    }

}
