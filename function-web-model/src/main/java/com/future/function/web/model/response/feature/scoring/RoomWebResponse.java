package com.future.function.web.model.response.feature.scoring;

import com.future.function.web.model.response.feature.core.UserWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomWebResponse {

  private String id;

  private UserWebResponse student;

  private AssignmentWebResponse assignment;

  private Integer point;

}
