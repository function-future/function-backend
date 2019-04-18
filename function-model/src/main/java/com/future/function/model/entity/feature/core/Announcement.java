package com.future.function.model.entity.feature.core;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
  @Builder.Default
  private String id = UUID.randomUUID()
    .toString();
  
  @Field(FieldName.Announcement.TITLE)
  @NotBlank(message = "NotBlank")
  private String title;
  
  @Field(FieldName.Announcement.SUMMARY)
  @Max(value = 70,
       message = "Max")
  private String summary;
  
  @Field(FieldName.Announcement.DESCRIPTION_HTML)
  @NotNull(message = "NotNull")
  private String descriptionHtml;
  
  @Field(FieldName.Announcement.FILE)
  @NotNull(message = "NotNull")
  private File file;
  
}
