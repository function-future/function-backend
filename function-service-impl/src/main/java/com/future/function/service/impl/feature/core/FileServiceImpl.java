package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.File;
import com.future.function.repository.feature.core.FileRepository;
import com.future.function.service.api.feature.core.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
  
  private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
    ".jpg", ".jpeg", ".png");
  
  private static final String BASE_URL = "http://localhost:8080/files/resource";
  
  private static final String BASE_PATH = "C:\\function\\files\\static";
  
  private static final String URL_SEPARATOR = "/";
  
  private static final String PATH_SEPARATOR = java.io.File.separator;
  
  private static final String THUMBNAIL_SUFFIX = "-thumbnail";
  
  private static final String DOT = ".";
  
  private final FileRepository fileRepository;
  
  @Autowired
  public FileServiceImpl(FileRepository fileRepository) {
    
    this.fileRepository = fileRepository;
  }
  
  @Override
  public File getFile(String id) {
    
    return Optional.ofNullable(fileRepository.findOne(id))
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }
  
  @Override
  public byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin) {
    
    return fileRepository.findByIdAndAsResource(getFileId(fileName),
                                                fileOrigin.isAsResource()
    )
      .map(file -> getFileOrThumbnail(file, fileName))
      .map(this::getBytesFromJavaIoFile)
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }
  
  private String getFileId(String fileName) {
    
    return fileName.substring(0, 36);
  }
  
  private java.io.File getFileOrThumbnail(File file, String fileName) {
    
    return Optional.of(fileName)
      .filter(this::isThumbnailName)
      .map(result -> new java.io.File(file.getThumbnailPath()))
      .orElseGet(() -> new java.io.File(file.getFilePath()));
  }
  
  private boolean isThumbnailName(String name) {
    
    int maxNonThumbnailFilenameLength =
      36 + DOT.length() + FilenameUtils.getExtension(name)
        .length();
    return name.length() > maxNonThumbnailFilenameLength;
  }
  
  @Override
  public File storeFile(MultipartFile multipartFile, FileOrigin fileOrigin) {
    
    String id = UUID.randomUUID()
      .toString();
    String extension = DOT + FilenameUtils.getExtension(
      multipartFile.getOriginalFilename())
      .toLowerCase();
    
    String folderPath = constructPathOrUrl(BASE_PATH, PATH_SEPARATOR,
                                           fileOrigin.lowCaseValue(),
                                           PATH_SEPARATOR, id
    );
    
    Image thumbnailImage;
    String thumbnailPath;
    String thumbnailUrl;
    switch (fileOrigin) {
      case USER:
        if (!IMAGE_EXTENSIONS.contains(extension)) {
          throw new BadRequestException("Invalid Extension");
        }
        
        thumbnailImage = toThumbnailImage(multipartFile);
        thumbnailPath = constructPathOrUrl(
          folderPath, PATH_SEPARATOR, id, THUMBNAIL_SUFFIX, extension);
        thumbnailUrl = constructPathOrUrl(toOriginFolderName(fileOrigin),
                                          URL_SEPARATOR, id, THUMBNAIL_SUFFIX,
                                          extension
        );
        break;
      default:
        throw new BadRequestException("Invalid File Origin");
    }
    
    java.io.File folder = Paths.get(folderPath)
      .toFile();
    if (!folder.exists()) {
      folder.mkdirs();
    }
    
    String filePath = constructPathOrUrl(
      folderPath, PATH_SEPARATOR, id, extension);
    
    java.io.File ioFile = new java.io.File(filePath);
    java.io.File thumbnailIoFile = new java.io.File(thumbnailPath);
    
    try {
      multipartFile.transferTo(ioFile);
      
      ImageIO.write(toBufferedThumbnailImage(thumbnailImage),
                    extension.substring(1), thumbnailIoFile
      );
    } catch (IOException e) {
      throw new BadRequestException("Unsupported Operation");
    }
    
    String fileUrl = constructPathOrUrl(toOriginFolderName(fileOrigin),
                                        URL_SEPARATOR, id, extension
    );
    
    File file = File.builder()
      .id(id)
      .filePath(filePath)
      .fileUrl(fileUrl)
      .thumbnailPath(thumbnailPath)
      .thumbnailUrl(thumbnailUrl)
      .asResource(fileOrigin.isAsResource())
      .markFolder(false)
      .build();
    
    return fileRepository.save(file);
  }
  
  private String toOriginFolderName(FileOrigin fileOrigin) {
    
    return BASE_URL + URL_SEPARATOR + fileOrigin.lowCaseValue();
  }
  
  @Override
  @SuppressWarnings("unused")
  public void deleteFile(String id) {
    
    boolean deleted = Optional.of(id)
      .filter(this::deleteFileFromDisk)
      .map(result -> {
        fileRepository.delete(id);
        return true;
      })
      .orElse(false);
  }
  
  private boolean deleteFileFromDisk(String id) {
    
    return Optional.of(id)
      .map(this::constructPathOrUrl)
      .map(Paths::get)
      .map(Path::toFile)
      .filter(java.io.File::exists)
      .map(FileSystemUtils::deleteRecursively)
      .orElseThrow(() -> new BadRequestException("Invalid Path Given"));
  }
  
  private String constructPathOrUrl(String id) {
    
    return constructPathOrUrl(BASE_PATH, PATH_SEPARATOR, id, "");
  }
  
  private String constructPathOrUrl(
    String base, String separator, String id, String extension
  ) {
    
    return constructPathOrUrl(base, separator, id, "", extension);
  }
  
  private String constructPathOrUrl(
    String base, String separator, String id, String thumbnailOrSeparator,
    String extensionOrId
  ) {
    
    return base + separator + id + thumbnailOrSeparator + extensionOrId;
  }
  
  private BufferedImage toBufferedThumbnailImage(Image thumbnailImage) {
    
    BufferedImage bufferedThumbnailImage = new BufferedImage(
      thumbnailImage.getWidth(null), thumbnailImage.getHeight(null),
      BufferedImage.TYPE_INT_ARGB
    );
    
    Graphics2D bufferedThumbnailImageGraphics =
      bufferedThumbnailImage.createGraphics();
    bufferedThumbnailImageGraphics.drawImage(thumbnailImage, 0, 0, null);
    bufferedThumbnailImageGraphics.dispose();
    return bufferedThumbnailImage;
  }
  
  private Image toThumbnailImage(MultipartFile multipartFile) {
    
    try {
      return ImageIO.read(multipartFile.getInputStream())
        .getScaledInstance(150, 150, BufferedImage.SCALE_SMOOTH);
    } catch (IOException e) {
      throw new BadRequestException("Unacceptable File for Thumbnail");
    }
  }
  
  private byte[] getBytesFromJavaIoFile(java.io.File ioFile) {
    
    try {
      return IOUtils.toByteArray(ioFile.toURI());
    } catch (IOException e) {
      throw new BadRequestException("Unsupported Operation");
    }
  }
  
}
