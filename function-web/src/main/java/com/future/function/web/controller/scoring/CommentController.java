package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.web.mapper.request.scoring.CommentRequestMapper;
import com.future.function.web.mapper.response.scoring.CommentResponseMapper;
import com.future.function.web.model.request.scoring.CommentWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.CommentWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/assignments/{assignmentId}/rooms/{roomId}/comments")
public class CommentController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private CommentRequestMapper requestMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<List<CommentWebResponse>> findAllCommentsByRoomId(@PathVariable(value = "roomId") String roomId) {
        return CommentResponseMapper
                .toDataListCommentWebResponse(roomService.findAllCommentsByRoomId(roomId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<CommentWebResponse> createComment(@PathVariable(value = "roomId") String roomId,
                                                          @RequestBody CommentWebRequest webRequest) {
        return CommentResponseMapper
                .toDataCommentWebResponse(
                        HttpStatus.CREATED,
                        roomService
                                .createComment(requestMapper
                                        .toCommentFromRequestWithRoomId(webRequest, roomId)
                                ));
    }

}
