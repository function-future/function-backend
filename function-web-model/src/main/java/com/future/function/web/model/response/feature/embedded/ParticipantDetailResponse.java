package com.future.function.web.model.response.feature.embedded;

import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDetailResponse {

  private String id;

  private String name;

  private String avatar;

  private String university;

  private BatchWebResponse batch;

  private String type;

}
