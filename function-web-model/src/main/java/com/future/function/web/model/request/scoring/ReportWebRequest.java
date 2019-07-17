package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportWebRequest {

    @NotBlank(message = "NotBlank")
    private String name;

    @NotBlank(message = "NotBlank")
    private String description;

    @Min(value = 1, message = "Min")
    private long usedAt;

    private List<String> students;

}
