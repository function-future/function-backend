package com.future.function.service.impl.feature.core.scheduler;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Optional;

@Component
public class FileDeleteScheduler {
  
  static final int HALF_HOUR = 1000 * 60 * 30;
  
  private final FileRepositoryV2 fileRepository;
  
  public FileDeleteScheduler(FileRepositoryV2 fileRepository) {
    
    this.fileRepository = fileRepository;
  }
  
  @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
  public void deleteFileOnSchedule() {
    
    fileRepository.findAllByUsedFalse()
      .forEachOrdered(this::deleteFileRecursivelyAndFromDatabase);
  }
  
  private void deleteFileRecursivelyAndFromDatabase(FileV2 fileV2) {
    
    Optional.ofNullable(fileV2)
      .filter(this::isCreatedAndUnusedMoreThan30Minutes)
      .ifPresent(file -> {
        this.deleteFileFromSystemIfNotFolder(file);
        fileRepository.delete(file);
      });
  }
  
  private void deleteFileFromSystemIfNotFolder(FileV2 fileV2) {
    
    Optional.of(fileV2)
      .filter(file -> !file.isMarkFolder())
      .ifPresent(file -> FileSystemUtils.deleteRecursively(
        new File(this.getContainingFolderPath(file))));
  }
  
  private boolean isCreatedAndUnusedMoreThan30Minutes(FileV2 fileV2) {
    
    Long createdAt = fileV2.getCreatedAt();
    long timeDiff = Math.abs(createdAt - System.currentTimeMillis());
    
    return timeDiff >= HALF_HOUR;
  }
  
  private String getContainingFolderPath(FileV2 fileV2) {
    
    String filePath = fileV2.getFilePath();
    
    return filePath.substring(0, filePath.lastIndexOf(File.separator));
  }
  
}
