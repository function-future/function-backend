package com.future.function.model.entity.sample;

import org.springframework.data.mongodb.core.mapping.Document;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.DocumentName;
import com.future.function.validation.annotation.InitialValidationAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.INITIAL_ENTITY)
public class InitialEntity extends BaseEntity {

  /**
   * See information about the annotation in this {@link InitialValidationAnnotation}
   * <p>
   * Example of validating using annotation
   */
  @InitialValidationAnnotation(value = "defaultValue")
  private String s;

}