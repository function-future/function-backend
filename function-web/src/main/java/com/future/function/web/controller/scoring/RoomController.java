package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
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
    
    private final FileProperties fileProperties;

    @Autowired
    public RoomController(
      AssignmentService assignmentService, FileProperties fileProperties
    ) {
        this.assignmentService = assignmentService;
        this.fileProperties = fileProperties;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<RoomWebResponse> findAllRoomsByAssignmentId(@PathVariable String assignmentId,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
        @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR}) Session session) {
        return RoomResponseMapper
                .toPagingRoomWebResponse(assignmentService
                        .findAllRoomsByAssignmentId(assignmentId,
                                                    PageHelper.toPageable(page, size)), fileProperties.getUrlPrefix());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<RoomWebResponse> findRoomById(@PathVariable String id,
        @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT}) Session session) {
        return RoomResponseMapper
            .toDataRoomWebResponse(assignmentService.findRoomById(id,
                                                                  session.getUserId()), fileProperties.getUrlPrefix());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<RoomWebResponse> updateRoomScore(@PathVariable String id,
                                                         @RequestBody RoomPointWebRequest request,
                                                         @WithAnyRole(roles = Role.MENTOR) Session session) {
        return RoomResponseMapper
                .toDataRoomWebResponse(assignmentService
                    .giveScoreToRoomByRoomId(id, session.getUserId(),
                                             request.getPoint()),
                                       fileProperties.getUrlPrefix());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse deleteRoomById(@PathVariable String id, @WithAnyRole(roles = Role.ADMIN) Session session) {
        assignmentService.deleteRoomById(id);
        return ResponseHelper.toBaseResponse(HttpStatus.OK);
    }

}
