package com.future.function.web.mapper.request.scoring;

import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.RoomPointWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomRequestMapper {

  @Autowired
  private RequestValidator requestValidator;

  public RoomPointWebRequest validate(RoomPointWebRequest request) {

    return requestValidator.validate(request);
  }

}
