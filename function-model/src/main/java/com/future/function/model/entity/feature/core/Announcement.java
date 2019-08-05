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
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Entity representation for announcements.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.ANNOUNCEMENT)
public class Announcement extends BaseEntity {
  
  @Id
  private String id;
  
  @Field(FieldName.Announcement.TITLE)
  private String title;
  
  @Field(FieldName.Announcement.SUMMARY)
  private String summary;
  
  @Field(FieldName.Announcement.DESCRIPTION)
  private String description;
  
  @DBRef
  @Field(FieldName.Announcement.FILE)
  private List<FileV2> fileV2s;
  
}
