package com.future.function.session.model;

import com.future.function.common.enumeration.core.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {
  
  @Builder.Default
  private String id = UUID.randomUUID()
    .toString();
  
  private String userId;
  
  private String batchId;
  
  private String email;
  
  private Role role;
  
}
