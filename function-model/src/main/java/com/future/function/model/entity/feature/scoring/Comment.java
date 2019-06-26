package com.future.function.model.entity.feature.scoring;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DocumentName.COMMENT)
public class Comment extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @DBRef(lazy = true)
    @Field(FieldName.Comment.AUTHOR)
    private User author;

    @Field(FieldName.Comment.TEXT)
    private String text;

    @DBRef(lazy = true)
    @Field(FieldName.Comment.ROOM)
    private Room room;

}