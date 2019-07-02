package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.scoring.RoomResponseMapper;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/assignments/{assignmentId}/students/{studentId}/rooms")
public class StudentRoomController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<RoomWebResponse> findAllRoomsByStudentId(@PathVariable(value = "studentId") String studentId,
                                                                   @RequestParam(required = false, defaultValue = "1") int page,
                                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return RoomResponseMapper.toPagingRoomWebResponse(
                assignmentService.findAllRoomsByStudentId(studentId, PageHelper.toPageable(page, size)));
    }

}
