package com.future.function.web.model.request.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class copyAssignmentWebRequest {

    @NotBlank(message = "NotBlank")
    private String targetBatch;

    @NotBlank(message = "NotBlank")
    private String assignmentId;

}
