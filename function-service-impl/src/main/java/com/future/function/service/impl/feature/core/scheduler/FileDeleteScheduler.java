package com.future.function.service.impl.feature.core.scheduler;

import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.FileRepositoryV2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Optional;

@Component
public class FileDeleteScheduler {

  private final FileRepositoryV2 fileRepository;

  private final FileProperties fileProperties;

  public FileDeleteScheduler(
    FileRepositoryV2 fileRepository, FileProperties fileProperties
  ) {

    this.fileRepository = fileRepository;
    this.fileProperties = fileProperties;
  }

  @Scheduled(fixedDelayString = "#{@fileProperties.schedulerActivePeriod}")
  public void deleteFileOnSchedule() {

    fileRepository.findAllByUsedFalse()
      .forEachOrdered(this::deleteFileRecursivelyAndFromDatabase);
  }

  private void deleteFileRecursivelyAndFromDatabase(FileV2 fileV2) {

    Optional.ofNullable(fileV2)
      .filter(this::isCreatedAndUnusedMoreThanDefinedPeriod)
      .ifPresent(file -> {
        this.deleteFileFromSystemIfNotFolder(file);
        fileRepository.delete(file);
      });
  }

  private void deleteFileFromSystemIfNotFolder(FileV2 fileV2) {

    Optional.of(fileV2)
      .filter(file -> !file.isMarkFolder())
      .ifPresent(this::deleteFileFromSystem);
  }

  private void deleteFileFromSystem(FileV2 file) {

    if (CollectionUtils.isEmpty(file.getVersions())) {
      this.deleteFileContainerFolder(
        this.getContainingFolderPath(file.getFilePath()));
    } else {
      this.deleteFileVersions(file);
    }
  }

  private void deleteFileVersions(FileV2 file) {

    file.getVersions()
      .values()
      .forEach(version -> this.deleteFileContainerFolder(
        this.getContainingFolderPath(version.getPath())));
  }

  private void deleteFileContainerFolder(String filePath) {

    FileSystemUtils.deleteRecursively(new File(filePath));
  }

  private String getContainingFolderPath(String filePath) {

    return filePath.substring(0, filePath.lastIndexOf(File.separator));
  }

  private boolean isCreatedAndUnusedMoreThanDefinedPeriod(FileV2 fileV2) {

    long createdAt = fileV2.getCreatedAt();
    long timeDiff = Math.abs(createdAt - System.currentTimeMillis());

    return timeDiff >= fileProperties.getMinimumFileCreatedPeriod();
  }

}
