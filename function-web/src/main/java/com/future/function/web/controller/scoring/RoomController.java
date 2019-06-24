package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.scoring.RoomResponseMapper;
import com.future.function.web.model.request.scoring.RoomPointWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/assignments/{assignmentId}/rooms")
public class RoomController {

    private AssignmentService assignmentService;

    @Autowired
    public RoomController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR})
    public PagingResponse<RoomWebResponse> findAllRoomsByAssignmentId(@PathVariable(value = "assignmentId") String assignmentId,
                                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return RoomResponseMapper
                .toPagingRoomWebResponse(assignmentService
                        .findAllRoomsByAssignmentId(assignmentId, PageHelper.toPageable(1, 10)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})
    public DataResponse<RoomWebResponse> findRoomById(@PathVariable(value = "id") String id, Session session) {
        return RoomResponseMapper
            .toDataRoomWebResponse(assignmentService.findRoomById(id, session.getId()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @WithAnyRole(roles = Role.MENTOR)
    public DataResponse<RoomWebResponse> updateRoomScore(@PathVariable(value = "id") String id,
        @RequestBody RoomPointWebRequest request, Session session) {
        return RoomResponseMapper
                .toDataRoomWebResponse(assignmentService
                    .giveScoreToRoomByRoomId(id, session.getId(), request.getPoint()));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @WithAnyRole(roles = Role.ADMIN)
    public BaseResponse deleteRoomById(@PathVariable(value = "id") String id) {
        assignmentService.deleteRoomById(id);
        return ResponseHelper.toBaseResponse(HttpStatus.OK);
    }

}
