package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.web.model.request.core.BatchWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchRequestMapper {
  
  private final ObjectValidator validator;
  
  @Autowired
  public BatchRequestMapper(ObjectValidator validator) {
    
    this.validator = validator;
  }
  
  public Batch toBatch(BatchWebRequest request) {
    
    return this.toValidatedBatch(null, request);
  }
  
  private Batch toValidatedBatch(String id, BatchWebRequest request) {
    
    validator.validate(request);
    
    return Batch.builder()
      .id(id)
      .name(request.getName())
      .code(request.getCode())
      .build();
  }
  
  public Batch toBatch(String id, BatchWebRequest request) {
    
    return this.toValidatedBatch(id, request);
  }
  
}
