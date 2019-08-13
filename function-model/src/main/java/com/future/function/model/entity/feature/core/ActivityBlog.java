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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.ACTIVITY_BLOG)
public class ActivityBlog extends BaseEntity {
  
  @Id
  private String id;
  
  @Field(FieldName.ActivityBlog.TITLE)
  private String title;
  
  @Field(FieldName.ActivityBlog.DESCRIPTION)
  private String description;
  
  @Field(FieldName.ActivityBlog.USER)
  @DBRef(lazy = true)
  private User user;
  
  @Field(FieldName.ActivityBlog.FILES)
  @DBRef(lazy = true)
  private List<FileV2> files;
  
}
