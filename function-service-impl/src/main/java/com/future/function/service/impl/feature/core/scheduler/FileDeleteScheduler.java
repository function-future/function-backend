package com.future.function.service.impl.feature.core.scheduler;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@Component
public class FileDeleteScheduler {
  
  private final FileRepositoryV2 fileRepository;
  
  public FileDeleteScheduler(FileRepositoryV2 fileRepository) {
    
    this.fileRepository = fileRepository;
  }
  
  @Scheduled(fixedDelay = 60 * 60 * 24)
  public void deleteFileOnSchedule() {
    
    fileRepository.findAllByUsedFalse()
      .forEachOrdered(this::deleteFileRecursivelyAndFromDatabase);
  }
  
  private void deleteFileRecursivelyAndFromDatabase(FileV2 fileV2) {
    
    FileSystemUtils.deleteRecursively(
      new File(this.getContainingFolderPath(fileV2)));
    
    fileRepository.delete(fileV2);
  }
  
  private String getContainingFolderPath(FileV2 fileV2) {
    
    String filePath = fileV2.getFilePath();
    
    return filePath.substring(0, filePath.lastIndexOf("/"));
  }
  
}
