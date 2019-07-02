package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Report extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private LocalDate usedAt;

    private List<String> studentIds;

}
