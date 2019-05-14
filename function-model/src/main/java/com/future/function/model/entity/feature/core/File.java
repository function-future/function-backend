package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entity representation for files.
 *
 * @deprecated
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
  
  @Field(FieldName.File.FILE_PATH)
  private String filePath;
  
  @Field(FieldName.File.FILE_URL)
  private String fileUrl;
  
  @Field(FieldName.File.THUMBNAIL_PATH)
  @Builder.Default
  private String thumbnailPath = "";
  
  @Field(FieldName.File.THUMBNAIL_URL)
  @Builder.Default
  private String thumbnailUrl = "";
  
  @Field(FieldName.File.MARK_FOLDER)
  private boolean markFolder;
  
  @Field(FieldName.File.AS_RESOURCE)
  private boolean asResource;
  
}
