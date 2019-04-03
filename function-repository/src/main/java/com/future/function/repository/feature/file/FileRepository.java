package com.future.function.repository.feature.file;

import com.future.function.model.entity.feature.file.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, String> {
  
  Optional<File> findByIdAndAsResource(String id, boolean asResource);
  
}
