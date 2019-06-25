package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.FileV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository class for file database operations.
 */
@Repository
public interface FileRepositoryV2 extends MongoRepository<FileV2, String> {
  
  /**
   * Finds a file given its id and mark as resource.
   *
   * @param id         Id of to be searched {@link FileV2}
   * @param asResource Mark of a file, whether a file serves as a resource or
   *                   a dynamic file.
   *
   * @return {@code Optional<FileV2>} - File found in database, if any
   * exists; otherwise returns {@link Optional#empty()}.
   */
  Optional<FileV2> findByIdAndAsResource(String id, boolean asResource);
  
  /**
   * Finds all file/folder given parentId.
   *
   * @param parentId Id of parent of file(s)/folder(s).
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<FileV2>} - FileV2 objects found in database, if any
   * exists.
   */
  Page<FileV2> findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDesc(
    String parentId, Pageable pageable
  );
  
  /**
   * Finds all file/folder given parentId.
   *
   * @param parentId Id of parent of file(s)/folder(s).
   *
   * @return {@code List<FileV2>} - FileV2 objects found in database, if any
   * exists.
   */
  List<FileV2> findAllByParentIdAndDeletedFalse(String parentId);
  
  /**
   * Finds a file/folder by its id and parentId.
   *
   * @param id       Id of file/folder to-be-retrieved.
   * @param parentId Id of parent of file/folder.
   *
   * @return {@code Optional<FileV2>} - File found in database, if any
   * exists; otherwise returns {@link Optional#empty()}.
   */
  Optional<FileV2> findByIdAndParentIdAndDeletedFalse(String id, String parentId);
  
  /**
   * Finds all file by its marking used is false.
   *
   * @return {@code Stream<FileV2>} - FileV2 objects found in database, if any
   * exists.
   */
  Stream<FileV2> findAllByUsedFalse();
  
}
