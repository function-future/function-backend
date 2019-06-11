package com.future.function.web.model.request.communication;

import com.future.function.validation.annotation.communication.ChatroomName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 15:40 01/06/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomRequest {

  @ChatroomName(message = "InvalidName")
  private String name;

  @NotEmpty(message = "NotEmpty")
  private List<String> members;

}
