package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Comment;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.CommentWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentRequestMapper {

    private RequestValidator validator;

    @Autowired
    public CommentRequestMapper(RequestValidator validator) {
        this.validator = validator;
    }

    public Comment toCommentFromRequestWithRoomId(CommentWebRequest request, String roomId) {
        return toValidatedComment(request, roomId);
    }

    private Comment toValidatedComment(CommentWebRequest request, String roomId) {
        request = validator.validate(request);
        return Comment
                .builder()
                .room(Room.builder().id(roomId).build())
                .text(request.getComment())
                .build();
    }

}
