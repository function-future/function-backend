package com.future.function.web.mapper.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class WebRequestMapper {
  
  private final ObjectMapper objectMapper;
  
  @Autowired
  public WebRequestMapper(ObjectMapper objectMapper) {
    
    this.objectMapper = objectMapper;
  }
  
  public <T> T toWebRequestObject(String data, Class<T> type) {
    
    T request;
    try {
      request = objectMapper.readValue(data, type);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: ", e);
      throw new BadRequestException("Bad Request");
    }
    return request;
  }
  
}
