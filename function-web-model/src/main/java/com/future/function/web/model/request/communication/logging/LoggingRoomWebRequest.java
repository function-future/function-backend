package com.future.function.web.model.request.communication.logging;

import com.future.function.validation.annotation.communication.LoggingRoomTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Author : Ricky Kennedy
 * Created At : 23:12 27/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggingRoomWebRequest {
  @LoggingRoomTitle(message = "Invalid Title")
  private String title;

  @Length(min= 1, max = 200, message = "Length")
  private String description;

  @NotEmpty(message = "NotEmpty")
  private List<String> members;
}
