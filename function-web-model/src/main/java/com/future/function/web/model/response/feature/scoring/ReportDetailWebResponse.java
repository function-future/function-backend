package com.future.function.web.model.response.feature.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.future.function.web.model.response.base.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDetailWebResponse {

  private String studentId;

  private String studentName;

  private String batchCode;

  private String university;

  private String avatar;

  private Integer point;

  private Integer totalPoint;

  private Paging paging;

  private List<SummaryWebResponse> scores;

}
