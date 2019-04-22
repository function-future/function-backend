package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representation for files.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.FILE)
public class File extends BaseEntity {

  @Id
  private String id;

  @NotBlank(message = "NotBlank")
  private String filePath;

  @NotBlank(message = "NotBlank")
  private String fileUrl;

  @Builder.Default
  private String thumbnailPath = "";

  @Builder.Default
  private String thumbnailUrl = "";

  private boolean markFolder;

  private boolean asResource;

}
