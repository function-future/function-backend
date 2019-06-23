package com.future.function.web.model.response.feature.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailEmbeddedWebResponse {

    private String id;
    private String type;
    private String name;
    private int point;

}
