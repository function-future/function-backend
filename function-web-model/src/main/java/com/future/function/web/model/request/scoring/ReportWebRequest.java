package com.future.function.web.model.request.scoring;

import javax.validation.constraints.Size;
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

    @Size(min = 2, max = 3, message = "Min_2_Max_3")
    private List<String> students;

}
