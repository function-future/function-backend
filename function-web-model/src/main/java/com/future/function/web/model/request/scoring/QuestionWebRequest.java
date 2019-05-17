package com.future.function.web.model.request.scoring;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWebRequest {

    @NotBlank(message = "NotBlank")
    private String text;

    @NotEmpty(message = "NotEmpty")
    @Size(message = "Size")
    private List<OptionWebRequest> options;

}
