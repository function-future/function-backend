package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for file database operations.
 */
@Repository
public interface FileRepository extends MongoRepository<File, String> {
  
  /**
   * Finds a file given its id and mark as resource.
   *
   * @param id         Id of to be searched {@link File}
   * @param asResource Mark of a file, whether a file serves as a resource or
   *                   a dynamic file.
   *
   * @return {@code Optional<File>} - File found in database, if any
   * exists; otherwise returns {@link java.util.Optional#empty()}.
   */
  Optional<File> findByIdAndAsResource(String id, boolean asResource);
  
}
