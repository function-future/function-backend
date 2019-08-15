package com.future.function.model.entity.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.ACCESS)
public class Access {

  @Id
  private String id;

  @Field(FieldName.Access.URL_REGEX)
  private String urlRegex;

  @Field(FieldName.Access.ROLE)
  private Role role;

  @Field(FieldName.Access.COMPONENTS)
  private Map<String, Object> components;

}
