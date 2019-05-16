package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.web.mapper.request.core.MultipartFileRequestMapper;
import com.future.function.web.mapper.response.core.ResourceResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
  
  private final ResourceService resourceService;
  
  private final MultipartFileRequestMapper multipartFileRequestMapper;
  
  public ResourceController(
    ResourceService resourceService,
    MultipartFileRequestMapper multipartFileRequestMapper
  ) {
    
    this.resourceService = resourceService;
    this.multipartFileRequestMapper = multipartFileRequestMapper;
  }
  
  // TODO Put name in request body
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileWebResponse> createFile(
    @RequestParam(required = false)
      MultipartFile file,
    @RequestParam
      String origin,
    @RequestParam(required = false)
      String name
  ) {
    
    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(file);
    
    return ResourceResponseMapper.toResourceDataResponse(
      resourceService.storeFile(name, pair.getFirst(), pair.getSecond(),
                                FileOrigin.toFileOrigin(origin)
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
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
