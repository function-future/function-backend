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
public class CopyStudentQuizWebRequest {

    @NotNull(message = "NotNull")
    private int originBatch;

    @NotNull(message = "NotNull")
    private int targetBatch;

    @NotEmpty(message = "Not Empty")
    private String quizId;

}
