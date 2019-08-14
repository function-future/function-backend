package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.FileV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface FileRepositoryV2 extends MongoRepository<FileV2, String> {
  
  Optional<FileV2> findByIdAndAsResource(String id, boolean asResource);
  
  Page<FileV2> findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDesc(
    String parentId, Pageable pageable
  );
  
  List<FileV2> findAllByParentIdAndDeletedFalse(String parentId);
  
  Optional<FileV2> findByIdAndParentIdAndDeletedFalse(
    String id, String parentId
  );

  Stream<FileV2> findAllByUsedFalse();
  
  Optional<FileV2> findByIdAndDeletedFalse(String id);
  
}
