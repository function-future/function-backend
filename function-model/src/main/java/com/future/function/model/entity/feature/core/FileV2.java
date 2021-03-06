package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.embedded.FilePath;
import com.future.function.model.entity.feature.core.embedded.Version;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

  @Field(FieldName.File.PARENT_ID)
  private String parentId;

  @Field(FieldName.File.USED)
  private boolean used;

  @Field(FieldName.File.MARK_FOLDER)
  private boolean markFolder;

  @Field(FieldName.File.AS_RESOURCE)
  private boolean asResource;

  @Field(FieldName.File.VERSIONS)
  @Builder.Default
  private Map<Long, Version> versions = new LinkedHashMap<>();

  @Field(FieldName.File.USER)
  private User user;

  @Field(FieldName.File.PATHS)
  private List<FilePath> paths;

}
