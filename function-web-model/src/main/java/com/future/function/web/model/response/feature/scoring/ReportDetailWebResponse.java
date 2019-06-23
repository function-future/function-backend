package com.future.function.web.model.response.feature.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailWebResponse {

    private String id;
    private String studentName;
    private String batchCode;
    private List<ReportDetailEmbeddedWebResponse> scores;

}
