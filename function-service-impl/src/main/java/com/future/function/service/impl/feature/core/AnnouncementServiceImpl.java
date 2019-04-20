package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.feature.core.AnnouncementRepository;
import com.future.function.service.api.feature.core.AnnouncementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service implementation class for announcement logic operations
 * implementation.
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
  
  private final AnnouncementRepository announcementRepository;
  
  @Autowired
  public AnnouncementServiceImpl(
    AnnouncementRepository announcementRepository
  ) {
    
    this.announcementRepository = announcementRepository;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<Announcement>} - Page of announcements found in
   * database.
   */
  @Override
  public Page<Announcement> getAnnouncements(Pageable pageable) {
    
    return announcementRepository.findAll(pageable);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param announcementId Id of announcement to be retrieved.
   *
   * @return {@code Announcement} - The announcement object found in database.
   */
  @Override
  public Announcement getAnnouncement(String announcementId) {
    
    return Optional.ofNullable(announcementId)
      .map(announcementRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get Announcement Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param announcement Announcement data of new announcement.
   * @param file         File to be attached to announcement. May be null.
   *
   * @return {@code Announcement} - The announcement object of the saved data.
   */
  @Override
  public Announcement createAnnouncement(
    Announcement announcement, MultipartFile file
  ) {
    
    announcementRepository.save(announcement);
    
    //TODO Save attached file
    
    return announcementRepository.findOne(announcement.getId());
  }
  
  /**
   * {@inheritDoc}
   *
   * @param announcement Announcement data of new announcement.
   * @param file         File to be attached to announcement. May be null.
   *
   * @return {@code Announcement} - The announcement object of the saved data.
   */
  @Override
  public Announcement updateAnnouncement(
    Announcement announcement, MultipartFile file
  ) {
    
    //TODO Save attached file
    
    return Optional.of(announcement)
      .map(Announcement::getId)
      .map(announcementRepository::findOne)
      .map(foundAnnouncement -> copyPropertiesAndSaveAnnouncement(announcement,
                                                                  foundAnnouncement
      ))
      .orElseThrow(
        () -> new NotFoundException("Update Announcement Not Found"));
  }
  
  private Announcement copyPropertiesAndSaveAnnouncement(
    Announcement announcement, Announcement foundAnnouncement
  ) {
    
    BeanUtils.copyProperties(announcement, foundAnnouncement,
                             FieldName.BaseEntity.CREATED_AT,
                             FieldName.BaseEntity.VERSION
    );
    return announcementRepository.save(foundAnnouncement);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param announcementId Id of announcement to be deleted.
   */
  @Override
  public void deleteAnnouncement(String announcementId) {
    
    Optional.ofNullable(announcementId)
      .map(this::getAnnouncement)
      .ifPresent(announcementRepository::delete);
  }
  
}
