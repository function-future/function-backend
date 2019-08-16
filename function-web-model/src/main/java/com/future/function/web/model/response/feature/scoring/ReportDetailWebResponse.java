package com.future.function.web.model.response.feature.scoring;

import com.fasterxml.jackson.annotation.JsonInclude;
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

  private List<SummaryWebResponse> scores;

  private Integer point;

  private Integer totalPoint;

}
