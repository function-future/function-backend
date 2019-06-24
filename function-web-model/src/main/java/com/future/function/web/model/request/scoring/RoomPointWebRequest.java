package com.future.function.web.model.request.scoring;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomPointWebRequest {

  @Min(value = 0, message = "Min")
  @NotNull(message = "NotNull")
  private Integer point;

}
