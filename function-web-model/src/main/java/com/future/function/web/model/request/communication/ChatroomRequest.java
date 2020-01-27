package com.future.function.web.model.request.communication;

import com.future.function.validation.annotation.communication.ChatroomName;
import com.future.function.validation.annotation.core.FileMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomRequest {

  @ChatroomName(message = "InvalidName")
  private String name;

  @FileMustExist
  @Size(min= 0, max = 1)
  private List<String> picture;

  @NotEmpty(message = "NotEmpty")
  private List<String> members;

}
