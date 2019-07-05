package com.future.function.web.model.response.feature.scoring;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailWebResponse {

    private String studentName;
    private String batchCode;
  private String university;
  private List<SummaryWebResponse> scores;

}
