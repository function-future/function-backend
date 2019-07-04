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
@Document(collection = DocumentName.ROOM)
public class Room extends BaseEntity {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @DBRef(lazy = true)
    @Field(FieldName.Room.STUDENT)
    private User student;

    @DBRef(lazy = true)
    @Field(FieldName.Room.ASSIGNMENT)
    private Assignment assignment;

    @Field(FieldName.Room.POINT)
    private Integer point;

}