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

  public static DataResponse<RoomWebResponse> toDataRoomWebResponse(
    Room room, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(HttpStatus.OK,
                                         buildRoomWebResponse(room, urlPrefix)
    );
  }

  private static RoomWebResponse buildRoomWebResponse(
    Room room, String urlPrefix
  ) {

    return RoomWebResponse.builder()
      .assignment(
        AssignmentResponseMapper.toAssignmentDataResponse(room.getAssignment(),
                                                          urlPrefix
        )
          .getData())
      .student(
        UserResponseMapper.toUserDataResponse(room.getStudent(), urlPrefix)
          .getData())
      .point(room.getPoint())
      .id(room.getId())
      .build();
  }

  public static PagingResponse<RoomWebResponse> toPagingRoomWebResponse(
    Page<Room> roomPage, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           buildRoomWebResponses(roomPage,
                                                                 urlPrefix
                                           ), PageHelper.toPaging(roomPage)
    );
  }

  private static List<RoomWebResponse> buildRoomWebResponses(
    Page<Room> roomPage, String urlPrefix
  ) {

    return roomPage.getContent()
      .stream()
      .map(room -> buildRoomWebResponse(room, urlPrefix))
      .collect(Collectors.toList());
  }

}
