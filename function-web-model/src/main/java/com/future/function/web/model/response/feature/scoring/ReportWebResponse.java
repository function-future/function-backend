package com.future.function.web.model.response.feature.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportWebResponse {

    private String id;
    private String title;
    private String description;
    private String batchCode;
    private long usedAt;
    private int studentCount;

}