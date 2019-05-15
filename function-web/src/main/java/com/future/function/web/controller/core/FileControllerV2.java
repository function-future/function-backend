package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.MultipartFileRequestMapper;
import com.future.function.web.model.response.base.DataResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resources")
public class FileControllerV2 {
  
  private final ResourceService resourceService;
  
  private final MultipartFileRequestMapper multipartFileRequestMapper;
  
  public FileControllerV2(
    ResourceService resourceService,
    MultipartFileRequestMapper multipartFileRequestMapper
  ) {
    
    this.resourceService = resourceService;
    this.multipartFileRequestMapper = multipartFileRequestMapper;
  }
  
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileV2> createFile(
    @RequestParam
      MultipartFile file,
    @RequestParam
      String origin
  ) {
    
    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(file);
    
    return ResponseHelper.toDataResponse(HttpStatus.CREATED,
                                         resourceService.storeFile(
                                           pair.getFirst(), pair.getSecond(),
                                           FileOrigin.toFileOrigin(origin)
                                         )
    );
  }
  
  @GetMapping(value = "/{origin}/{fileName}")
  public byte[] getFileAsByteArray(
    @PathVariable
      String origin,
    @PathVariable
      String fileName
  ) {
    
    return resourceService.getFileAsByteArray(
      fileName, FileOrigin.toFileOrigin(origin));
  }
  
}
