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

import java.util.UUID;

/**
 * Entity representation for files.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.FILE)
public class FileV2 extends BaseEntity {
  
  @Id
  @Builder.Default
  private String id = UUID.randomUUID()
    .toString();
  
  @Field(FieldName.File.FILE_PATH)
  private String filePath;
  
  @Field(FieldName.File.FILE_URL)
  private String fileUrl;
  
  @Field(FieldName.File.THUMBNAIL_PATH)
  @Builder.Default
  private String thumbnailPath = "";
  
  @Field(FieldName.File.THUMBNAIL_URL)
  private String thumbnailUrl;
  
  @Field(FieldName.File.NAME)
  @Builder.Default
  private String name = "";
  
  @Field(FieldName.File.USED)
  private boolean used;
  
  @Field(FieldName.File.MARK_FOLDER)
  private boolean markFolder;
  
  @Field(FieldName.File.AS_RESOURCE)
  private boolean asResource;
  
}
