package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionWebRequest {

    private String id;

    @NotBlank(message = "NotBlank")
    private String label;

    private Boolean correct;

}
