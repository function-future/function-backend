package com.future.function.web.mapper.response.scoring;

import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.SummaryWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreSummaryResponseMapper {

  public static DataResponse<SummaryWebResponse> toDataSummaryResponse(
    SummaryVO summaryVO
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, buildSummaryResponse(summaryVO));
  }

  private static SummaryWebResponse buildSummaryResponse(SummaryVO summaryVO) {

    return SummaryWebResponse.builder()
      .title(summaryVO.getTitle())
      .type(summaryVO.getType())
      .point(summaryVO.getPoint())
      .build();
  }

  public static PagingResponse<SummaryWebResponse> toDataListSummaryResponse(
    Page<SummaryVO> summaryVOPage
  ) {

    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, buildSummaryResponseList(summaryVOPage), PageHelper.toPaging(summaryVOPage));
  }

  private static List<SummaryWebResponse> buildSummaryResponseList(
    Page<SummaryVO> summaryVOPage
  ) {

    return summaryVOPage.getContent().stream()
      .map(ScoreSummaryResponseMapper::buildSummaryResponse)
      .collect(Collectors.toList());
  }

}
