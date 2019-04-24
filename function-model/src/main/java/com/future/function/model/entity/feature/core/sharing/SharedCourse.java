package com.future.function.model.entity.feature.core.sharing;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Course;
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

/**
 * Entity representation for course sharing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.SHARED_COURSE)
public class SharedCourse extends BaseEntity {
  
  @Id
  private String id;
  
  @Field(FieldName.SharedCourse.BATCH)
  @DBRef(lazy = true)
  private Batch batch;
  
  @Field(FieldName.SharedCourse.COURSE)
  @DBRef(lazy = true)
  private Course course;
  
}
