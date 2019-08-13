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
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.DISCUSSION)
public class Discussion extends BaseEntity {
  
  @Id
  private String id;
  
  @Field(FieldName.Discussion.DESCRIPTION)
  private String description;
  
  @Field(FieldName.Discussion.USER)
  @DBRef(lazy = true)
  private User user;
  
  @Field(FieldName.Discussion.COURSE_ID)
  private String courseId;
  
  @Field(FieldName.Discussion.BATCH_ID)
  private String batchId;
  
  @Transient
  private String batchCode;
  
}
