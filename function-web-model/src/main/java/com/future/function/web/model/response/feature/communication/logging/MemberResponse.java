package com.future.function.web.model.response.feature.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Ricky Kennedy
 * Created At : 9:53 28/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

  private String id;

  private String name;

  private String avatar;

  private String role;

  private String university;

  private String Batch;
}
