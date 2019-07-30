package com.future.function.web.mapper.request.core;

import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.FileWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileRequestMapper {
  
  private final WebRequestMapper requestMapper;
  
  private final RequestValidator validator;
  
  @Autowired
  public FileRequestMapper(
    WebRequestMapper requestMapper, RequestValidator validator
  ) {
    
    this.requestMapper = requestMapper;
    this.validator = validator;
  }
  
  public FileWebRequest toFileWebRequest(String json, byte[] bytes) {
    
    FileWebRequest request = requestMapper.toWebRequestObject(
      json, FileWebRequest.class);
    
    request.setBytes(bytes);
    
    return validator.validate(request);
  }
  
  public FileWebRequest toFileWebRequest(String id, String json, byte[] bytes) {
    
    FileWebRequest request = requestMapper.toWebRequestObject(
      json, FileWebRequest.class);
    
    request.setId(id);
    request.setBytes(bytes);
    
    return validator.validate(request);
  }
  
}
