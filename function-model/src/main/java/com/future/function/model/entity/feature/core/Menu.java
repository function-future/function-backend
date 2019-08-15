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

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.MENU)
public class Menu {

  @Id
  private String id;

  @Field(FieldName.Menu.ROLE)
  private Role role;

  @Field(FieldName.Menu.SECTIONS)
  private Map<String, Object> sections = new HashMap<>();

}
