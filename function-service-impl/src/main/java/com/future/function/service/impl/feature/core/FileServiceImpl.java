package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.File;
import com.future.function.repository.feature.core.FileRepository;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.impl.helper.ByteArrayHelper;
import org.apache.commons.io.FilenameUtils;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation class for file logic operations definition.
 */
@Service
public class FileServiceImpl implements FileService {
  
  private static final String URL_SEPARATOR = "/";
  
  private static final String PATH_SEPARATOR = java.io.File.separator;
  
  private static final String DOT = ".";
  
  private final FileRepository fileRepository;
  
  private final List<String> imageExtensions;
  
  private final String urlPrefix;
  
  private final String storagePath;
  
  private final String thumbnailSuffix;
  
  @Autowired
  public FileServiceImpl(
    FileRepository fileRepository, FileProperties fileProperties
  ) {
    
    this.fileRepository = fileRepository;
    
    imageExtensions = fileProperties.getImageExtensions();
    urlPrefix = fileProperties.getUrlPrefix();
    storagePath = fileProperties.getStoragePath();
    thumbnailSuffix = fileProperties.getThumbnailSuffix();
  }
  
  /**
   * {@inheritDoc}
   *
   * @param id Id of file to be retrieved.
   *
   * @return {@code File} - The file object found in database.
   */
  @Override
  public File getFile(String id) {
    
    return Optional.ofNullable(fileRepository.findOne(id))
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param fileName   Name of the file to be obtained.
   * @param fileOrigin Origin of the file, which in this case specifies at
   *                   which folder does the file reside in the file storage.
   *
   * @return {@code byte[]} - The byte array of the file.
   */
  @Override
  public byte[] getFileAsByteArray(String fileName, FileOrigin fileOrigin) {
    
    return fileRepository.findByIdAndAsResource(getFileId(fileName),
                                                fileOrigin.isAsResource()
    )
      .map(file -> getFileOrThumbnail(file, fileName))
      .map(ByteArrayHelper::getBytesFromJavaIoFile)
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
  
  /**
   * {@inheritDoc}
   *
   * @param multipartFile The original file sent from client side.
   * @param fileOrigin    Origin of the file, which in this case specifies at
   *                      which folder the file will reside in the file
   *                      storage.
   *
   * @return {@code File} - The file object of the saved data.
   */
  @Override
  public File storeFile(MultipartFile multipartFile, FileOrigin fileOrigin) {
    
    String id = UUID.randomUUID()
      .toString();
    String extension = DOT + FilenameUtils.getExtension(
      multipartFile.getOriginalFilename())
      .toLowerCase();
    
    String folderPath = constructPathOrUrl(storagePath, PATH_SEPARATOR,
                                           fileOrigin.lowCaseValue(),
                                           PATH_SEPARATOR, id
    );
    
    Image thumbnailImage;
    String thumbnailPath;
    String thumbnailUrl;
    switch (fileOrigin) {
      case USER:
        if (!imageExtensions.contains(extension)) {
          throw new BadRequestException("Invalid Extension");
        }
        
        thumbnailImage = toThumbnailImage(multipartFile);
        thumbnailPath = constructPathOrUrl(
          folderPath, PATH_SEPARATOR, id, thumbnailSuffix, extension);
        thumbnailUrl = constructPathOrUrl(toDetailedUrlPrefix(fileOrigin),
                                          URL_SEPARATOR, id, thumbnailSuffix,
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
    
    String fileUrl = constructPathOrUrl(toDetailedUrlPrefix(fileOrigin),
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
  
  private String toDetailedUrlPrefix(FileOrigin fileOrigin) {
    
    return urlPrefix + URL_SEPARATOR + fileOrigin.lowCaseValue();
  }
  
  /**
   * {@inheritDoc}
   *
   * @param id Id of the file to be deleted.
   */
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
  
  private boolean isThumbnailName(String name) {
    
    int maxNonThumbnailFilenameLength =
      36 + DOT.length() + FilenameUtils.getExtension(name)
        .length();
    return name.length() > maxNonThumbnailFilenameLength;
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
    
    return constructPathOrUrl(storagePath, PATH_SEPARATOR, id, "");
  }
  
}
