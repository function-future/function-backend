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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.COURSE)
public class Course extends BaseEntity {
  
  @Id
  @Builder.Default
  private String id = UUID.randomUUID()
    .toString();
  
  @Field(FieldName.Course.TITLE)
  private String title;
  
  @Field(FieldName.Course.DESCRIPTION)
  private String description;
  
  @Field(FieldName.Course.FILE)
  private FileV2 file;
  
}
