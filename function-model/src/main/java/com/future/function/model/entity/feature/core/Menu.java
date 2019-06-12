package com.future.function.model.entity.feature.core;

import com.future.function.common.enumeration.core.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
  
  @Id
  private String id;
  
  private Role role;
  
  private Map<String, Object> sections = new HashMap<>();
  
}
