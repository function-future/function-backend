package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQuizWebRequest {

    @NotNull(message = "NotNull")
    private int batchCode;

    @NotEmpty(message = "NotEmpty")
    private String quizId;

}
