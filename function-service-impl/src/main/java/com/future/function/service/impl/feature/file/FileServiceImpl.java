package com.future.function.service.impl.feature.file;

import com.future.function.common.enumeration.FileOrigin;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.file.File;
import com.future.function.repository.feature.file.FileRepository;
import com.future.function.service.api.feature.file.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
  
  private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
    "jpg", "jpeg", "png");
  
  private static final String BASE_URL = "http://localhost:8080/resource";
  
  private static final String BASE_PATH = "C:\\function\\images";
  
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
  public File getFile(String id, FileOrigin fileOrigin) {
    
    return fileRepository.findByIdAndAsResource(id, fileOrigin.isAsResource())
      .orElseThrow(() -> new NotFoundException("Get File Not Found"));
  }
  
  @Override
  public File storeFile(MultipartFile multipartFile, FileOrigin origin) {
    
    String id = UUID.randomUUID()
      .toString();
    String extension = DOT + FilenameUtils.getExtension(
      multipartFile.getOriginalFilename())
      .toLowerCase();
    
    String folderPath = constructPathOrUrl(BASE_PATH, PATH_SEPARATOR,
                                           origin.name()
                                             .toLowerCase(), PATH_SEPARATOR, id
    );
    
    Image thumbnailImage;
    String thumbnailPath;
    String thumbnailUrl;
    switch (origin) {
      case USER:
        if (!IMAGE_EXTENSIONS.contains(extension)) {
          throw new BadRequestException("Invalid Extension");
        }
        
        thumbnailImage = toThumbnailImage(multipartFile);
        thumbnailPath = constructPathOrUrl(
          folderPath, PATH_SEPARATOR, id, THUMBNAIL_SUFFIX, extension);
        thumbnailUrl = constructPathOrUrl(BASE_URL, URL_SEPARATOR, id,
                                          THUMBNAIL_SUFFIX, extension
        );
        break;
      default:
        throw new BadRequestException("Invalid File Origin");
    }
    
    if (!Files.exists(Paths.get(folderPath))) {
      new java.io.File(folderPath).mkdir();
    }
    
    String filePath = constructPathOrUrl(
      folderPath, PATH_SEPARATOR, id, extension);
    
    java.io.File file = new java.io.File(filePath);
    java.io.File thumbnailFile = new java.io.File(thumbnailPath);
    
    try {
      multipartFile.transferTo(file);
      
      ImageIO.write(toBufferedThumbnailImage(thumbnailImage),
                    extension.substring(1), thumbnailFile
      );
    } catch (IOException e) {
      throw new BadRequestException("Unsupported Operation");
    }
    
    String fileUrl = constructPathOrUrl(BASE_URL, URL_SEPARATOR, id, extension);
    
    return File.builder()
      .id(id)
      .filePath(filePath)
      .fileUrl(fileUrl)
      .thumbnailPath(thumbnailPath)
      .thumbnailUrl(thumbnailUrl)
      .asResource(origin.isAsResource())
      .markFolder(false)
      .build();
  }
  
  @Override
  public void deleteFile(String id) {
    
    String path = constructPathOrUrl(BASE_PATH, PATH_SEPARATOR, id);
    
    if (Files.exists(Paths.get(path))) {
      
      java.io.File folder = new java.io.File(path);
      
      Arrays.stream(Objects.requireNonNull(folder.listFiles()))
        .map(java.io.File::delete);
      
      folder.delete();
    }
    
    fileRepository.delete(id);
  }
  
  private String constructPathOrUrl(
    String base, String separator, String id
  ) {
    
    return constructPathOrUrl(base, separator, id, "");
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
  
}
