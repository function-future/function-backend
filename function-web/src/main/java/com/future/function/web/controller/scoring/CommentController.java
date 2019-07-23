package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.scoring.CommentRequestMapper;
import com.future.function.web.mapper.response.scoring.CommentResponseMapper;
import com.future.function.web.model.request.scoring.CommentWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.CommentWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/assignments/{assignmentId}/rooms/{roomId}/comments")
public class CommentController {

    private RoomService roomService;

    private CommentRequestMapper requestMapper;

    @Autowired
    public CommentController(RoomService roomService, CommentRequestMapper requestMapper) {
        this.roomService = roomService;
        this.requestMapper = requestMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<CommentWebResponse> findAllCommentsByRoomId(@PathVariable String roomId,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size,

        @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT}) Session session) {
        return CommentResponseMapper
                .toPagingCommentWebResponse(roomService.findAllCommentsByRoomId(roomId, PageHelper.toPageable(page, size)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<CommentWebResponse> createComment(@PathVariable String roomId,
                                                          @RequestBody CommentWebRequest webRequest,
        @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT}) Session session) {
        return CommentResponseMapper
                .toDataCommentWebResponse(
                        HttpStatus.CREATED,
                    roomService.createComment(
                        requestMapper.toCommentFromRequestWithRoomId(webRequest, roomId),
                        session.getUserId()));
    }

}
