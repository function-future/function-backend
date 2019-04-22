package com.future.function.model.entity.feature;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Entity representation for sequences.
 * <p>
 * Contains generated sequence for an object of a specified type of sequence in
 * database. The {@link #id} field contains the name of the specified type of
 * sequence, and the {@link #sequenceNumber} field is the generated value of
 * sequence for the {@link #id}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.SEQUENCE)
public class Sequence extends BaseEntity {

  @Id
  private String id;

  @Field(value = FieldName.SEQUENCE_NUMBER)
  private long sequenceNumber;

}
