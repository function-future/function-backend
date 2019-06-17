package com.future.function.repository.listener.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.Version;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileMongoEventListener extends AbstractMongoEventListener<FileV2> {
  
  @Override
  public void onBeforeConvert(BeforeConvertEvent<FileV2> event) {
    
    super.onBeforeConvert(event);
    
    Optional.of(event)
      .map(MongoMappingEvent::getSource)
      .filter(file -> !file.isMarkFolder())
      .ifPresent(this::updateFileVersions);
  }
  
  private void updateFileVersions(FileV2 file) {
    
    long nextVersion = file.getVersion() + 1;
    
    file.getVersions()
      .put(nextVersion, this.buildVersion(file, nextVersion));
  }
  
  private Version buildVersion(FileV2 file, long nextVersion) {
    
    return Version.builder()
      .path(file.getFilePath())
      .url(file.getFileUrl() + "?version=" + nextVersion)
      .timestamp(System.currentTimeMillis())
      .build();
  }
  
}
