package com.future.function.web.model.response.feature.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

/**
 * Author : Ricky Kennedy
 * Created At : 23:04 27/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggingRoomResponse {

  private String id;

  private String description;

  private List<MemberResponse> members;

}
