package com.future.function.web.model.response.feature.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggingRoomWebResponse {

  private String id;

  private String title;

  private String description;

  private List<MemberResponse> members;

}
