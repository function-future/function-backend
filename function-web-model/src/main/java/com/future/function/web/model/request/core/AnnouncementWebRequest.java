package com.future.function.web.model.request.core;

import com.future.function.validation.annotation.core.FileMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementWebRequest {
  
  @NotBlank(message = "NotBlank")
  private String title;
  
  @Size(max = 70,
        message = "Size")
  private String summary;
  
  @NotNull(message = "NotNull")
  private String description;
  
  @FileMustExist
  private List<String> files;
  
}
