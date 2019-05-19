package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuestionWebRequest {

    @NotEmpty(message = "NotEmpty")
    private String text;

    @NotNull(message = "NotNull")
    @Size(max = 1, min = 1, message = "Size")
    private OptionWebRequest option;

}
