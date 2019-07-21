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
public class ReportWebResponse {

    private String id;
    private String title;
    private String description;
    private String batchCode;
    private List<String> studentIds;
    private long usedAt;
    private int studentCount;

}
