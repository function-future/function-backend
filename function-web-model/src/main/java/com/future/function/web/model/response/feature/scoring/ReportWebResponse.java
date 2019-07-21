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
    private String name;
    private String description;
    private String batchCode;
    private int studentCount;
    private long uploadedDate;
    private List<String> studentIds;

}
