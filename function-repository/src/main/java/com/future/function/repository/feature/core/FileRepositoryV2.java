package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.FileV2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
  
}
