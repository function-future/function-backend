package com.future.function.web.model.request.core;

import com.future.function.common.data.core.FileData;
import com.future.function.validation.annotation.core.TypeAndBytesMustBeValid;
import com.future.function.validation.annotation.core.TypeMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeAndBytesMustBeValid
public class FileWebRequest implements FileData {
  
  private String id;
  
  @NotBlank(message = "NotBlank")
  private String name;
  
  @TypeMustExist
  private String type;
  
  private byte[] bytes;
  
}
