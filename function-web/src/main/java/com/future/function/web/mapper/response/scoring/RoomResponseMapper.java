package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomResponseMapper {

  public static DataResponse<RoomWebResponse> toDataRoomWebResponse(Room room) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildRoomWebResponse(room));
  }

  public static PagingResponse<RoomWebResponse> toPagingRoomWebResponse(Page<Room> roomPage) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, buildRoomWebResponses(roomPage), PageHelper.toPaging(roomPage));
  }

  private static List<RoomWebResponse> buildRoomWebResponses(Page<Room> roomPage) {
    return roomPage.getContent()
        .stream()
        .map(RoomResponseMapper::buildRoomWebResponse)
        .collect(Collectors.toList());
  }

  private static RoomWebResponse buildRoomWebResponse(Room room) {
    return RoomWebResponse
        .builder()
        .assignment(AssignmentResponseMapper.toAssignmentDataResponse(room.getAssignment()).getData())
        .student(UserResponseMapper.toUserDataResponse(room.getStudent()).getData())
        .point(room.getPoint())
        .id(room.getId())
        .build();
  }

}
