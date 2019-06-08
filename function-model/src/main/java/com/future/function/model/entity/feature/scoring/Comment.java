package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @DBRef
    @Field
    private User author;

    @Field
    private String comment;

    @DBRef
    @Field
    private Room room;

}
