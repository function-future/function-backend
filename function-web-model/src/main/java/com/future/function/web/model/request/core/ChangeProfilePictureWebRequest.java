package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.FileMustBeImage;
import com.future.function.validation.annotation.core.FileMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProfilePictureWebRequest {

  @Size(max = 1,
        message = "Size")
  @FileMustExist
  @FileMustBeImage
  private List<String> avatar;

}
