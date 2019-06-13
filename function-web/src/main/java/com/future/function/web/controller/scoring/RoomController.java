package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.RoomService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scoring/assignments/{assignmentId}/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<RoomWebResponse> findAllRoomsByAssignmentId(@PathVariable(value = "assignmentId") String assignmentId,
                                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return RoomResponseMapper
                .toPagingRoomWebResponse(roomService
                        .findAllRoomsByAssignmentId(assignmentId, PageHelper.toPageable(1, 10)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<RoomWebResponse> findRoomById(@PathVariable(value = "id") String id) {
        return RoomResponseMapper
                .toDataRoomWebResponse(roomService.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<RoomWebResponse> updateRoomScore(@PathVariable(value = "id") String id, @RequestBody RoomPointWebRequest request) {
        return RoomResponseMapper
                .toDataRoomWebResponse(roomService
                        .giveScoreToRoomByRoomId(id, request.getPoint()));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse deleteRoomById(@PathVariable(value = "id") String id) {
        roomService.deleteRoomById(id);
        return ResponseHelper.toBaseResponse(HttpStatus.OK);
    }

}
