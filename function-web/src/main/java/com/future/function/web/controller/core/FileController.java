package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.FileService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.FileRequestMapper;
import com.future.function.web.mapper.request.core.MultipartFileRequestMapper;
import com.future.function.web.mapper.response.core.FileResponseMapper;
import com.future.function.web.model.request.core.FileWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/core/files")
public class FileController {
  
  private final FileService fileService;
  
  private final MultipartFileRequestMapper multipartFileRequestMapper;
  
  private final FileRequestMapper fileRequestMapper;
  
  @Autowired
  public FileController(
    FileService fileService,
    MultipartFileRequestMapper multipartFileRequestMapper,
    FileRequestMapper fileRequestMapper
  ) {
    
    this.fileService = fileService;
    this.multipartFileRequestMapper = multipartFileRequestMapper;
    this.fileRequestMapper = fileRequestMapper;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{parentId}")
  public PagingResponse<FileWebResponse> getFilesAndFolders(
    @PathVariable
      String parentId,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "10")
      int size
  
  ) {
    
    return FileResponseMapper.toFilePagingResponse(
      fileService.getFilesAndFolders(parentId,
                                     PageHelper.toPageable(page, size)
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{parentId}/{fileOrFolderId}")
  public DataResponse<FileWebResponse> getFileOrFolder(
    @PathVariable
      String parentId,
    @PathVariable
      String fileOrFolderId
  
  ) {
    
    return FileResponseMapper.toFileDataResponse(
      fileService.getFileOrFolder(fileOrFolderId, parentId));
  }
  
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/{parentId}",
               consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileWebResponse> createFileOrFolder(
    @PathVariable
      String parentId,
    @RequestParam(required = false)
      MultipartFile file,
    @RequestParam
      String data
  ) {
    
    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(file);
    
    FileWebRequest request = fileRequestMapper.toFileWebRequest(
      data, pair.getSecond());
    
    return FileResponseMapper.toFileDataResponse(
      HttpStatus.CREATED, fileService.createFileOrFolder(
        parentId, request.getName(), pair.getFirst(), pair.getSecond()));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{parentId}/{fileOrFolderId}",
              consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileWebResponse> updateFileOrFolder(
    @PathVariable
      String parentId,
    @PathVariable
      String fileOrFolderId,
    @RequestParam(required = false)
      MultipartFile file,
    @RequestParam
      String data,
    @RequestParam
      String email
  ) {
    
    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(file);
    
    FileWebRequest request = fileRequestMapper.toFileWebRequest(
      data, pair.getSecond());
    
    return FileResponseMapper.toFileDataResponse(
      fileService.updateFileOrFolder(email, fileOrFolderId, parentId,
                                     request.getName(), pair.getFirst(),
                                     pair.getSecond()
      ));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{parentId}/{fileOrFolderId}")
  public BaseResponse deleteFileOrFolder(
    @PathVariable
      String parentId,
    @PathVariable
      String fileOrFolderId,
    @RequestParam
      String email
  ) {
    
    fileService.deleteFileOrFolder(email, parentId, fileOrFolderId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
