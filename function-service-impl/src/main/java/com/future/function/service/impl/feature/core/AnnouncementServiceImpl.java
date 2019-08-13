package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.feature.core.AnnouncementRepository;
import com.future.function.service.api.feature.core.AnnouncementService;
import com.future.function.service.api.feature.core.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
  
  private final AnnouncementRepository announcementRepository;
  
  private final ResourceService resourceService;
  
  @Autowired
  public AnnouncementServiceImpl(
    AnnouncementRepository announcementRepository,
    ResourceService resourceService
  ) {
    
    this.announcementRepository = announcementRepository;
    this.resourceService = resourceService;
  }
  
  @Override
  public Page<Announcement> getAnnouncements(Pageable pageable) {
    
    return announcementRepository.findAllByOrderByUpdatedAtDesc(pageable);
  }
  
  @Override
  public Announcement getAnnouncement(String announcementId) {
    
    return Optional.ofNullable(announcementId)
      .map(announcementRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get Announcement Not Found"));
  }
  
  @Override
  public Announcement createAnnouncement(Announcement announcement) {
    
    return Optional.of(announcement)
      .map(this::setFileV2s)
      .map(announcementRepository::save)
      .orElse(announcement);
  }
  
  private Announcement setFileV2s(Announcement announcement) {
    
    List<String> fileIds = this.getFileIds(announcement);
    
    List<FileV2> fileV2s = fileIds.stream()
      .map(resourceService::getFile)
      .collect(Collectors.toList());
    
    announcement.setFileV2s(fileV2s);
    
    resourceService.markFilesUsed(fileIds, true);
    
    return announcement;
  }
  
  private List<String> getFileIds(Announcement announcement) {
    
    return announcement.getFileV2s()
      .stream()
      .map(FileV2::getId)
      .collect(Collectors.toList());
  }
  
  @Override
  public Announcement updateAnnouncement(
    Announcement announcement
  ) {
    
    return Optional.of(announcement)
      .map(Announcement::getId)
      .map(announcementRepository::findOne)
      .map(foundAnnouncement -> this.copyPropertiesAndSaveAnnouncement(
        announcement, foundAnnouncement))
      .orElse(announcement);
  }
  
  @Override
  public void deleteAnnouncement(String announcementId) {
    
    Optional.ofNullable(announcementId)
      .map(announcementRepository::findOne)
      .map(foundAnnouncement -> resourceService.markFilesUsed(
        this.getFileIds(foundAnnouncement), false))
      .map(ignored -> announcementId)
      .ifPresent(announcementRepository::delete);
  }
  
  private Announcement copyPropertiesAndSaveAnnouncement(
    Announcement announcement, Announcement foundAnnouncement
  ) {
    
    resourceService.markFilesUsed(this.getFileIds(foundAnnouncement), false);
    this.setFileV2s(announcement);
    
    BeanUtils.copyProperties(announcement, foundAnnouncement,
                             FieldName.BaseEntity.CREATED_AT,
                             FieldName.BaseEntity.VERSION
    );
    
    return announcementRepository.save(foundAnnouncement);
  }
  
}
