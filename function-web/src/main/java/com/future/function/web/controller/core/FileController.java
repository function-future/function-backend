package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.FileRequestMapper;
import com.future.function.web.mapper.request.core.MultipartFileRequestMapper;
import com.future.function.web.mapper.response.core.FileResponseMapper;
import com.future.function.web.model.request.core.FileWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.DataPageResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/core/files")
@WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
public class FileController {

  private final FileService fileService;

  private final MultipartFileRequestMapper multipartFileRequestMapper;

  private final FileRequestMapper fileRequestMapper;

  private final FileProperties fileProperties;

  @Autowired
  public FileController(
    FileService fileService,
    MultipartFileRequestMapper multipartFileRequestMapper,
    FileRequestMapper fileRequestMapper, FileProperties fileProperties
  ) {

    this.fileService = fileService;
    this.multipartFileRequestMapper = multipartFileRequestMapper;
    this.fileRequestMapper = fileRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{parentId}")
  public DataPageResponse<FileWebResponse<List<FileContentWebResponse>>> getFilesAndFolders(
    Session session,
    @PathVariable
      String parentId,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "10")
      int size
  ) {

    return FileResponseMapper.toMultipleFileDataResponse(
      fileService.getFilesAndFolders(parentId,
                                     PageHelper.toPageable(page, size)
      ), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{parentId}/{fileOrFolderId}")
  public DataResponse<FileWebResponse<FileContentWebResponse>> getFileOrFolder(
    Session session,
    @PathVariable
      String parentId,
    @PathVariable
      String fileOrFolderId
  ) {

    return FileResponseMapper.toSingleFileDataResponse(
      fileService.getFileOrFolder(fileOrFolderId, parentId),
      fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/{parentId}",
               consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileWebResponse<FileContentWebResponse>> createFileOrFolder(
    Session session,
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

    return FileResponseMapper.toSingleFileDataResponse(HttpStatus.CREATED,
                                                       fileService.createFileOrFolder(
                                                         session, parentId,
                                                         request.getName(),
                                                         pair.getFirst(),
                                                         pair.getSecond()
                                                       ),
                                                       fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{parentId}/{fileOrFolderId}",
              consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileWebResponse<FileContentWebResponse>> updateFileOrFolder(
    Session session,
    @PathVariable
      String parentId,
    @PathVariable
      String fileOrFolderId,
    @RequestParam(required = false)
      MultipartFile file,
    @RequestParam
      String data
  ) {

    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(file);

    FileWebRequest request = fileRequestMapper.toFileWebRequest(
      fileOrFolderId, data, pair.getSecond());

    return FileResponseMapper.toSingleFileDataResponse(
      fileService.updateFileOrFolder(session, fileOrFolderId, parentId,
                                     request.getName(), pair.getFirst(),
                                     pair.getSecond()
      ), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{parentId}/{fileOrFolderId}")
  public BaseResponse deleteFileOrFolder(
    Session session,
    @PathVariable
      String parentId,
    @PathVariable
      String fileOrFolderId
  ) {

    fileService.deleteFileOrFolder(session, parentId, fileOrFolderId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
