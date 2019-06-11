package com.future.function.web.mapper.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Mapper class for incoming request for general mapping. Wraps
 * {@link ObjectMapper} bean for better code and testing.
 */
@Slf4j
@Component
public class WebRequestMapper {
  
  private final ObjectMapper objectMapper;
  
  @Autowired
  public WebRequestMapper(ObjectMapper objectMapper) {
    
    this.objectMapper = objectMapper;
  }
  
  /**
   * Maps given JSON data in parameter {@code json} to an object of {@code
   * type} specified in method parameter.
   *
   * @param json JSON data to be mapped.
   * @param type Type of object to be returned.
   * @param <T>  Type of class of the specified JSON
   *
   * @return T - The mapped object.
   */
  public <T> T toWebRequestObject(String json, Class<T> type) {
    
    T request;
    try {
      request = objectMapper.readValue(json, type);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: ", e);
      throw new BadRequestException("Bad Request");
    }
    return request;
  }
  
}
