package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.Sequence;
import com.future.function.model.util.constant.FieldName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Sequence generator class for generating sequence of a given sequence name.
 */
@Component
public class SequenceGenerator {
  
  private final MongoOperations mongoOperations;
  
  @Autowired
  public SequenceGenerator(MongoOperations mongoOperations) {
    
    this.mongoOperations = mongoOperations;
  }
  
  /**
   * Increments a sequence's value given its name.
   *
   * @param sequenceName Name of the sequence to be incremented
   *
   * @return {@code long} - The incremented value of the specified sequence.
   */
  public long increment(String sequenceName) {
    
    Sequence sequence = mongoOperations.findAndModify(
      query(where("_id").is(sequenceName)),
      new Update().inc(FieldName.Sequence.SEQUENCE_NUMBER, 1),
      options().returnNew(true)
        .upsert(true), Sequence.class
    );
    
    return Optional.ofNullable(sequence)
      .map(Sequence::getSequenceNumber)
      .orElse(1L);
  }
  
}
